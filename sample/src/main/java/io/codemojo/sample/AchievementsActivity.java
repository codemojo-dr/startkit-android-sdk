package io.codemojo.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class AchievementsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        ImageView badge = (ImageView) findViewById(R.id.achievementBadge);
        TextView badgeName = (TextView) findViewById(R.id.lblBadgeName);

        badgeName.setText(getIntent().getStringExtra("label"));

        getSupportActionBar().setTitle("Achievement unlocked");

        String name = getIntent().getStringExtra("badge");
        int badgeImage = getResources().getIdentifier("achivement_" + name,"drawable", getPackageName());
        if(badgeImage > 0) {
            badge.setImageDrawable(getResources().getDrawable(badgeImage));
        }
    }
}
