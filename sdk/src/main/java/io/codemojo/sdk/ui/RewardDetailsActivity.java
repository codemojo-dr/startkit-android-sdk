package io.codemojo.sdk.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.R;
import io.codemojo.sdk.facades.CodemojoException;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.BrandGrabbedOffer;
import io.codemojo.sdk.models.BrandReward;
import io.codemojo.sdk.models.RewardsScreenSettings;
import io.codemojo.sdk.services.RewardsService;

public class RewardDetailsActivity extends AppCompatActivity implements CodemojoException, View.OnClickListener {

    RewardsService rewardsService;
    private RewardsScreenSettings settings;
    private BrandReward reward;
    private ProgressDialog progressDialog;
    private AlertDialog builder;

    private RewardsFlowReceiver receiver = new RewardsFlowReceiver();

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(getString(R.string.intent_rewards_ui)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = (RewardsScreenSettings) getIntent().getSerializableExtra("settings");

        if(settings.shouldAnimateScreenLoad()){
            overridePendingTransition(R.anim.up_from_bottom, 0);
        }

        setContentView(R.layout.activity_reward_details);

        assert getSupportActionBar() != null;
        try {
            if(getSupportActionBar() != null) getSupportActionBar().hide();
        } catch (Exception ignored) {
        }

        if(settings.getThemeButtonColor() > 0) {
            findViewById(R.id.codemojo_dialog_grab_button).setBackgroundDrawable(getApplicationContext().getResources().getDrawable(settings.getThemeButtonColor()));
        }

        if(settings.getThemeAccentFontColor() > 0) {
            ((Button) findViewById(R.id.codemojo_dialog_grab_button)).setTextColor(getApplicationContext().getResources().getColor(settings.getThemeAccentFontColor()));
        }

        rewardsService = Codemojo.getRewardsService();
        rewardsService.setErrorHandler(this);
        reward = (BrandReward) getIntent().getSerializableExtra("reward");

        ImageView view = (ImageView) findViewById(R.id.banner);
        Picasso.with(this).load(reward.getLogo()).into(view);

        ((TextView) findViewById(R.id.lblTitle)).setText(reward.getOffer());
        ((TextView) findViewById(R.id.lblFinePrint)).setText(reward.getFineprint());
        ((TextView) findViewById(R.id.lblRedeemProcedure)).setText(reward.getRedemptionProcess());
        ((TextView) findViewById(R.id.lblSupport)).setText(reward.getSupport());

        Button btnAction = (Button) findViewById(R.id.codemojo_dialog_grab_button);
        if(!settings.isAllowGrab()){
            btnAction.setText(R.string.complete_milestone);
        }

        btnAction.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        /*
         * Check if reward grab is allowed
         */
        if(!settings.isAllowGrab()) {
            if (settings.getViewMilestoneListener() != null) {
                Intent grabIntent = new Intent();
                grabIntent.putExtra("reward_details", reward);

                grabIntent.setAction(Codemojo.ON_VIEW_MILESTONE_CLICK);
                if (settings.getViewMilestoneListener().onClick(grabIntent)) {
                    return;
                }
            }
            if(settings.getMilesStones() != null) {
                startActivity(new Intent(RewardDetailsActivity.this, RewardsMilestonesActivity.class) {{ putExtra("settings", settings); }});
            }
            return;
        } else if (settings.getRewardGrabListener() != null) {
            Intent grabIntent = new Intent();
            grabIntent.putExtra("reward_details", reward);

            grabIntent.setAction(Codemojo.ON_REWARD_GRAB_CLICK);
            if (settings.getViewMilestoneListener().onClick(grabIntent)) {
                return;
            }

        }

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rewards_claim_input, null);

        final EditText input = (EditText) dialogView.findViewById(R.id.txtCommunicationChannel);
        if(!settings.getCommunicationChannel().equals("")){
            input.setText(settings.getCommunicationChannel());
            if(!settings.shouldWaitForUserInput()){
                claimReward(input);
                return;
            }
        }

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                claimReward(input);
                return true;
            }
        });

        builder = new AlertDialog.Builder(RewardDetailsActivity.this)
                .setView(dialogView)
                .setPositiveButton("Claim Reward", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        claimReward(input);
                    }
                }).create();
        builder.show();
    }

    private void claimReward(final EditText input) {
        if(input.getText().toString().trim().isEmpty() || input.getText().toString().length() <= 5){
            Toast.makeText(this, "Valid Email ID or Mobile number required to receive the coupon", Toast.LENGTH_SHORT).show();
            return;
        }

        if(getWindow() != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        View view = RewardDetailsActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Map<String, String> details = new HashMap<String, String>();
        details.put("communicate", settings.isSendCouponAutomatically()? "1": "0");
        details.put("testing", settings.isTest()? "1": "0");
        if(settings.getLocale() != null) {
            details.put("locale", settings.getLocale());
        }
        if(settings.getLatitude() > 0) details.put("lat", String.valueOf(settings.getLatitude()));
        if(settings.getLongitude() > 0) details.put("lon", String.valueOf(settings.getLongitude()));
        progressDialog = ProgressDialog.show(RewardDetailsActivity.this, "", "Please wait ...");
        rewardsService.grabReward(reward.getId(), input.getText().toString(), details, new ResponseAvailable() {
            @Override
            public void available(Object result) {
                if(progressDialog != null) progressDialog.dismiss();
                if(builder != null) builder.dismiss();
                BrandGrabbedOffer go = (BrandGrabbedOffer) result;
                Intent data = new Intent();
                data.putExtra("reward", go);
                data.putExtra("reward_details", reward);
                data.putExtra("communication_channel", input.getText().toString());
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(progressDialog != null) progressDialog.dismiss();
        if(builder != null) builder.dismiss();

        setResult(Activity.RESULT_FIRST_USER);
        super.onBackPressed();
    }

    @Override
    public void onError(Exception exception) {
        Intent data = new Intent();
        data.putExtra("error", exception.getMessage());
        setResult(Activity.RESULT_CANCELED, data);
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();

        if(progressDialog != null) progressDialog.dismiss();
        if(builder != null) builder.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            setResult(Activity.RESULT_FIRST_USER);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        if(progressDialog != null) progressDialog.dismiss();
        if(builder != null) builder.dismiss();
        super.finish();
        if(settings.shouldAnimateScreenLoad()) {
            overridePendingTransition(0, R.anim.hide_from_top);
        }
    }


    public class RewardsFlowReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(getString(R.string.intent_rewards_ui)) && intent.getBooleanExtra("exit_flow", false)){
                finish();
            }
        }
    }

}
