package io.codemojo.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.facades.MessageReceivedHandler;
import io.codemojo.sdk.facades.RewardsAvailability;
import io.codemojo.sdk.facades.RewardsDialogListener;
import io.codemojo.sdk.models.BrandGrabbedOffer;
import io.codemojo.sdk.models.BrandReward;
import io.codemojo.sdk.models.Milestone;
import io.codemojo.sdk.models.ReferralScreenSettings;
import io.codemojo.sdk.models.RewardsScreenSettings;

public class ChooserActivity extends AppCompatActivity implements View.OnClickListener, MessageReceivedHandler, RewardsDialogListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        /*
         * Get the user id to use in the sample app
         */
        final EditText text = (EditText) findViewById(R.id.editClientID);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(!textView.getText().toString().trim().equals("")){

                    if(getWindow() != null) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        View view = ChooserActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }

                    /*
                     * Initialize the Objects
                     */
                    AppContext.init(ChooserActivity.this, textView.getText().toString());
                    AppContext.getCodemojoClient().getReferralService().useSignupReferral(ChooserActivity.this, null);
                    AppContext.getCodemojoClient().initRewardsService("a673fca0-91f9-11e6-a2dd-1b943448738e");

                    ((TextView) findViewById(R.id.lblReferralUsed)).setText(
                            "Referral Code used: " +
                                    AppContext.getCodemojoClient().getReferralService().getSignedUpReferralCode(ChooserActivity.this));

                    text.setVisibility(View.GONE);
                    findViewById(R.id.panelDemoChooser).setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(ChooserActivity.this, "Please type any username/email", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        findViewById(R.id.btnGamificationAchievements).setOnClickListener(this);
        findViewById(R.id.btnReferral).setOnClickListener(this);
        findViewById(R.id.btnRewards).setOnClickListener(this);

        getSupportActionBar().setTitle("Codemojo Demo");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReferral:
                /*
                 * Launch the referral screen
                 */
                ReferralScreenSettings settings = new ReferralScreenSettings("Codemojo Demo");
                String message = "Hi there, I have been using " + settings.getAppNameVariable() + " and" +
                        " found it very useful. \n\nWould be great if you can join me by clicking " +
                        settings.getUrlVariable() + " or use the code " + settings.getReferralCodeVariable();
                settings.setMessage(message);
                settings.setBanner(R.drawable.sample_invite);
                AppContext.getCodemojoClient().launchReferralScreen(settings);
                break;

            case R.id.btnGamificationAchievements:
                startActivity(new Intent(this, GamificationActivity.class));
                break;

            case R.id.btnRewards:
                Map<String, String> filters = new HashMap<>();
                filters.put("locale", "in");
                filters.put("testing", "true");
                Codemojo.getRewardsService().onRewardsAvailable(null, filters, new RewardsAvailability() {
                    ProgressDialog progressDialog;

                    @Override
                    public void processing() {
                        progressDialog = ProgressDialog.show(ChooserActivity.this, "", "Getting your reward...");
                        progressDialog.setCancelable(true);
                    }

                    @Override
                    public void available(List<BrandReward> rewards) {
                        progressDialog.dismiss();
                        RewardsScreenSettings settings = new RewardsScreenSettings();

                        settings.setRewardSelectListener(ChooserActivity.this);
                        settings.setTitleClickListener(ChooserActivity.this);
                        settings.setViewMilestoneClickListener(ChooserActivity.this);
                        settings.setRewardGrabListener(ChooserActivity.this);

                        settings.setRewardsSelectionPageTitle("<b>Congratulations!!</b> Please pick a Gift!");
                        settings.setAllowGrab(true);
                        settings.setWaitForUserInput(false);
                        settings.setCommunicationChannel("vb.shoaib@gmail.com");
                        settings.setTesting(true);
                        settings.setThemeTitleColor(R.color.white);
                        settings.setThemeButtonColor(R.color.colorAccent);
                        settings.setThemeTitleStripeColor(R.color.colorPrimaryDark);
                        settings.setThemeAccentFontColor(R.color.white);
                        settings.setLocale("in");
                        settings.setShouldShowCloseButton(true);

                        ArrayList<Milestone> milestones = new ArrayList<>();
                        milestones.add(new Milestone(0, "Each 50th time you open the app"));
                        milestones.add(new Milestone(0, "Every 10th time you post a facebook update"));
                        milestones.add(new Milestone(0, "Every time you refer it to your contacts"));

                        settings.setMileStones(milestones);

                        AppContext.getCodemojoClient().launchAvailableRewardsScreen(rewards, settings, this);
                    }

                    @Override
                    public void unavailable() {
                        Toast.makeText(ChooserActivity.this," Rewards not available for this location", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void grabbed(Intent data) {
                        Toast.makeText(ChooserActivity.this, "Your reward is on its way!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void error(String error) {
                        Toast.makeText(ChooserActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Codemojo.CODEMOJO_REWARD_USER){
            if(resultCode == Activity.RESULT_OK){
                BrandGrabbedOffer reward = (BrandGrabbedOffer) data.getSerializableExtra("reward");
                String communication = data.getStringExtra("communication_channel");
                Toast.makeText(this, "Coupon code " + reward.getCouponCode() + " for " + communication, Toast.LENGTH_SHORT).show();
            } else {
                if(data != null) {
                    String error = data.getStringExtra("error");
                    if (error != null && !error.isEmpty()) {
                        Toast.makeText(this, "Oops! " + error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public boolean onMessageReceived(Bundle data) {
        return false;
    }

    @Override
    public boolean onClick(Intent data, Context context) {
        // Handle stuffs here - return true if you are consuming the event
        Toast.makeText(this, "Hook: " + data.getAction(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, "Hook Error: " + error, Toast.LENGTH_SHORT).show();
    }
}
