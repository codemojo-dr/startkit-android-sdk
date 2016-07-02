package io.codemojo.sdk.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.R;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.ReferralCode;
import io.codemojo.sdk.models.ReferralScreenSettings;
import io.codemojo.sdk.services.ReferralService;
import io.codemojo.sdk.utils.Clipboard;

public class ReferralActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnInvite;
    private ReferralCode referral;
    private ReferralScreenSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);

        ReferralService referralService = new ReferralService(Codemojo.getAuthenticationService());

        settings = (ReferralScreenSettings) getIntent().getSerializableExtra("settings");

        getSupportActionBar().setTitle(settings.getPageTite());

        if(settings.getBanner() > 0) {
            Drawable drawable = getResources().getDrawable(settings.getBanner());
            if(drawable != null) {
                ((ImageView) findViewById(R.id.banner)).setImageDrawable(drawable);
            }
        }

        btnInvite = (Button) findViewById(R.id.btnInvite);
        btnInvite.setOnClickListener(this);
        btnInvite.setEnabled(false);
        btnInvite.setText(settings.getCallToActionTitle());

        findViewById(R.id.lblReferralCode).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Clipboard.setClipboard(ReferralActivity.this, referral.getCode());
                Toast.makeText(ReferralActivity.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return false;
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
        ((TextView) findViewById(R.id.lblReferralCode)).setText(referral.getCode().toUpperCase());
        settings.setReferralObject(referral);
    }

    @Override
    public void onClick(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, settings.getMessage());
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, settings.getSubjectLine());
        startActivity(Intent.createChooser(shareIntent, "Invite via"));
    }
}
