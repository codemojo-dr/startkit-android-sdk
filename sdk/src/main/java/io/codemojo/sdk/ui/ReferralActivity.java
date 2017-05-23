package io.codemojo.sdk.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.R;
import io.codemojo.sdk.facades.CodemojoException;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.ReferralCode;
import io.codemojo.sdk.models.ReferralScreenSettings;
import io.codemojo.sdk.services.ReferralService;
import io.codemojo.sdk.utils.Clipboard;

public class ReferralActivity extends AppCompatActivity implements View.OnClickListener, CodemojoException {

    private Button btnInvite;
    private ReferralCode referral;
    private ReferralService referralService;
    private ReferralScreenSettings settings;
    private EditText promoCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);

        referralService = new ReferralService(Codemojo.getAuthenticationService());

        referralService.setErrorHandler(this);

        settings = (ReferralScreenSettings) getIntent().getSerializableExtra("settings");

        getSupportActionBar().setTitle(settings.getPageTite());

        if(settings.getBanner() > 0) {
            Drawable drawable = getResources().getDrawable(settings.getBanner());
            if(drawable != null) {
                ((ImageView) findViewById(R.id.banner)).setImageDrawable(drawable);
            }
        }

        btnInvite = (Button) findViewById(R.id.btnInvite);
        promoCode = (EditText) findViewById(R.id.promoEnter);

        btnInvite.setOnClickListener(this);
        btnInvite.setEnabled(false);
        btnInvite.setText(settings.getCallToActionTitle());

        findViewById(R.id.lblEnterReferral).setOnClickListener(this);
        findViewById(R.id.btnProcessPromo).setOnClickListener(this);

        findViewById(R.id.lblReferralCode).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Clipboard.setClipboard(ReferralActivity.this, referral.getCode());
                Toast.makeText(ReferralActivity.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        promoCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                processPromoCode(promoCode.getText().toString());
                return true;
            }
        });

        referralService.getReferralCode(new ResponseAvailable() {
            @Override
            public void available(Object result) {
                btnInvite.setEnabled(true);
                referral = ((ReferralCode)result);
                onReferralAvailable();
            }
        });
    }

    private void onReferralAvailable(){
        settings.setReferralObject(referral);
        ((TextView) findViewById(R.id.lblReferralCode)).setText(referral.getCode().toUpperCase());
        ((TextView) findViewById(R.id.lblReferralDescription)).setText(settings.getReferralDesription());
    }

    private void processPromoCode(String promo){
        referralService.applyReferralCode(promo, new ResponseAvailable() {
            @Override
            public void available(Object result) {
                boolean status = (boolean) result;
                findViewById(R.id.panelEnterPromo).setVisibility(View.GONE);
                findViewById(R.id.lblEnterReferral).setVisibility(View.VISIBLE);
                if(status){
                    setResult(RESULT_OK);
                    Toast.makeText(ReferralActivity.this, "Congratulations! You have successfully applied the referral code", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_CANCELED);
                    Toast.makeText(ReferralActivity.this, "Sorry, this referral code cannot be applied", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btnInvite) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, settings.getMessage());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, settings.getSubjectLine());
            startActivity(Intent.createChooser(shareIntent, "Invite via"));
            setResult(RESULT_OK);
        } else if (i == R.id.lblEnterReferral) {
            findViewById(R.id.lblEnterReferral).setVisibility(View.GONE);
            findViewById(R.id.panelEnterPromo).setVisibility(View.VISIBLE);
        } else if (i == R.id.btnProcessPromo) {
            processPromoCode(promoCode.getText().toString());
        }
    }

    @Override
    public void onError(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
