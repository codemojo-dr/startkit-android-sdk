package io.codemojo.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

        AppContext.getCodemojoClient().getLoyaltyService().addLoyaltyPoints();
        getWalletBalance();

        getSupportActionBar().setTitle("Gamification Demo");
        findViewById(R.id.btnUno).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnCommunity).setOnClickListener(this);
        findViewById(R.id.btnLeader).setOnClickListener(this);
        findViewById(R.id.history).setOnClickListener(this);
    }

    private void getWalletBalance() {
        AppContext.getCodemojoClient().getWalletService().getWalletBalance(new ResponseAvailable() {
            @Override
            public void available(Object balance) {
                try {
                    ((TextView) findViewById(R.id.lblWalletBalance)).setText(
                            (int) ((WalletBalance) balance).getSlot3().getRawPoints() + " pts = $"
                                    + ((WalletBalance) balance).getSlot3().getConvertedPoints());
                } catch (Exception ignored) {
                    Log.e("gam_err", ignored.getMessage());
                }
            }
        });
    }

    @Override
    public void newTierUpgrade(String tierName) {
        Log.e("gam_tier", tierName);
        Toast.makeText(this, "You have been upgraded to new Tier: \"" + tierName + "\"!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void newBadgeUnlocked(int totalPoints, String badgeName) {
        Log.e("gam_badge", badgeName);
        Toast.makeText(this, "New Badge \"" + badgeName + "\" unlocked for you!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void newAchievementUnlocked(int totalAchievements, final String achievementName, final GamificationAchievement achievement) {
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
                settings.setRewardsSelectionPageTitle("<b>Congratulations, you have unlocked " + achievement.getLabel() + "</b><br/>"
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

            @Override
            public void grabbed(Intent data) {

            }

            @Override
            public void error(String error) {

            }
        });

        getWalletBalance();

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
        getWalletBalance();
        StringBuilder history = new StringBuilder();
        for (String achievement :
                achievements.keySet()) {
            history.append(achievement.toUpperCase()).append(":").append(" ").append(achievements.get(achievement).getTotal()).append("\n");
        }
        ((TextView) findViewById(R.id.lblAchievementHistory)).setText(history.toString());
        Log.e("gam_new", "something");
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
        getWalletBalance();
    }

    @Override
    public void onError(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
