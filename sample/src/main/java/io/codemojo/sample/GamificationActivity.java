package io.codemojo.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import io.codemojo.sdk.facades.GamificationEarnedEvent;
import io.codemojo.sdk.facades.LoyaltyEvent;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.GamificationAchievement;
import io.codemojo.sdk.models.WalletBalance;


public class GamificationActivity extends AppCompatActivity implements LoyaltyEvent, GamificationEarnedEvent, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppContext.getCodemojoClient().getReferralService().useSignupReferral(this, new ResponseAvailable() {
            @Override
            public void available(Object result) {
                if((boolean) result == false)
                    Toast.makeText(GamificationActivity.this, "Sorry, referral already claimed", Toast.LENGTH_SHORT).show();
            }
        });

        AppContext.getCodemojoClient().setGamificationEarnedEventListener(this);

        onActivityResult(0,0,null);

        getSupportActionBar().setTitle("Gamification Demo");
        findViewById(R.id.btnUno).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnCommunity).setOnClickListener(this);
        findViewById(R.id.btnLeader).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            AppContext.getCodemojoClient().getWalletService().getWalletBalance(new ResponseAvailable() {
                @Override
                public void available(Object balance) {
                    ((TextView) findViewById(R.id.lblWalletBalance)).setText((int) ((WalletBalance) balance).getSlot3().getRawPoints() + " pts");
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
    public void newAchievementUnlocked(int totalAchievements, String achievementName, GamificationAchievement achievement) {
        final Intent newBadge = new Intent(this, AchievementsActivity.class);
        newBadge.putExtra("badge", achievementName);
        newBadge.putExtra("label", achievement.getLabel());
        startActivityForResult(newBadge, 0);
    }

    @Override
    public void updatedAchievemenstAvailable(Map<String, GamificationAchievement> achievements) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUno:
                AppContext.getCodemojoClient().getGamificationService().captureAchievementsAction("uno");
                break;
            case R.id.btnStart:
                AppContext.getCodemojoClient().getGamificationService().captureAchievementsAction("start");
                break;
            case R.id.btnCommunity:
                AppContext.getCodemojoClient().getGamificationService().captureAchievementsAction("community");
                break;
            case R.id.btnLeader:
                AppContext.getCodemojoClient().getGamificationService().captureAchievementsAction("leader");
                break;
        }
    }
}