package io.codemojo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        ImageView badge = (ImageView) findViewById(R.id.achievementBadge);
        TextView badgeName = (TextView) findViewById(R.id.lblBadgeName);
        TextView pointsEarned = (TextView) findViewById(R.id.lblPoints);

        badgeName.setText(getIntent().getStringExtra("label"));
        pointsEarned.setText(getIntent().getIntExtra("points", 0) + " points earned");

        getSupportActionBar().setTitle("Achievement unlocked");

        String name = getIntent().getStringExtra("badge");
        int badgeImage = getResources().getIdentifier("achivement_" + name,"drawable", getPackageName());
        if(badgeImage > 0) {
            badge.setImageDrawable(getResources().getDrawable(badgeImage));
        }
    }
}
