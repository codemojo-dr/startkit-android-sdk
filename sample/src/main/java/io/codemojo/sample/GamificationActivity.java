package io.codemojo.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.facades.CodemojoException;
import io.codemojo.sdk.facades.GamificationEarnedEvent;
import io.codemojo.sdk.facades.LoyaltyEvent;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.facades.RewardsAvailability;
import io.codemojo.sdk.models.BrandReward;
import io.codemojo.sdk.models.GamificationAchievement;
import io.codemojo.sdk.models.RewardsScreenSettings;
import io.codemojo.sdk.models.WalletBalance;


public class GamificationActivity extends AppCompatActivity implements LoyaltyEvent, GamificationEarnedEvent,
        CodemojoException, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppContext.getCodemojoClient().setGamificationEarnedEventListener(this);
        AppContext.getCodemojoClient().getGamificationService().setErrorHandler(this);

        onActivityResult(0,0,null);

        getSupportActionBar().setTitle("Gamification Demo");
        findViewById(R.id.btnUno).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnCommunity).setOnClickListener(this);
        findViewById(R.id.btnLeader).setOnClickListener(this);
        findViewById(R.id.history).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            AppContext.getCodemojoClient().getWalletService().getWalletBalance(new ResponseAvailable() {
                @Override
                public void available(Object balance) {
                    ((TextView) findViewById(R.id.lblWalletBalance)).setText(
                            (int) ((WalletBalance) balance).getSlot3().getRawPoints() + " pts = $"
                    + ((WalletBalance) balance).getSlot3().getConvertedPoints());
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void newTierUpgrade(String tierName) {

    }

    @Override
    public void newBadgeUnlocked(int totalPoints, String badgeName) {
    }

    @Override
    public void newAchievementUnlocked(int totalAchievements, final String achievementName, GamificationAchievement achievement) {
        /*
         * Trigger the reward
         */
        Map<String, String> filters = new HashMap<>();
        filters.put("locale", "in");
        Codemojo.getRewardsService().onRewardsAvailable(null, filters, new RewardsAvailability() {
            ProgressDialog progressDialog;

            @Override
            public void processing() {
                progressDialog = ProgressDialog.show(GamificationActivity.this, "", "Getting your reward...");
                progressDialog.setCancelable(true);
            }

            @Override
            public void available(List<BrandReward> rewards) {
                progressDialog.dismiss();
                RewardsScreenSettings settings = new RewardsScreenSettings();
                settings.setAllowGrab(true);
                settings.setTesting(true);
                settings.setAnimateScreenLoad(true);
                settings.setRewardsSelectionPageTitle("Congratulations, you have unlocked " + achievementName.toUpperCase() + "\n"
                        + "Please treat yourself with a reward");
                settings.setThemeTitleStripeColor(R.color.colorAccent);
                settings.setThemeButtonColor(R.color.colorAccent);
                settings.setThemeAccentFontColor(R.color.white);
                AppContext.getCodemojoClient().launchAvailableRewardsScreen(rewards, settings, GamificationActivity.this);
            }

            @Override
            public void unavailable() {
                Toast.makeText(GamificationActivity.this," Rewards not available for this location", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        /*
         * Alternatively you can do any other stuff

                final Intent newBadge = new Intent(this, AchievementsActivity.class);
                newBadge.putExtra("badge", achievementName);
                newBadge.putExtra("label", achievement.getLabel());
                newBadge.putExtra("points", achievement.getPointsAdded());
                startActivityForResult(newBadge, 0);

         */
    }

    @Override
    public void updatedAchievemenstAvailable(Map<String, GamificationAchievement> achievements) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUno:
                AppContext.getCodemojoClient().getGamificationService().captureAchievementsAction("uno", null);
                break;
            case R.id.btnStart:
                AppContext.getCodemojoClient().getGamificationService().captureAchievementsAction("start", null);
                break;
            case R.id.btnCommunity:
                AppContext.getCodemojoClient().getGamificationService().captureAchievementsAction("community", null);
                break;
            case R.id.btnLeader:
                AppContext.getCodemojoClient().getGamificationService().captureAchievementsAction("leader", null);
                break;
            case R.id.history:
                AppContext.getCodemojoClient().launchGamificationTransactionScreen();
                break;
        }
    }

    @Override
    public void onError(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
