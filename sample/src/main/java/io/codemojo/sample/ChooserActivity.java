package io.codemojo.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.codemojo.sdk.models.ReferralScreenSettings;

public class ChooserActivity extends AppCompatActivity implements View.OnClickListener {

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

                    /*
                     * Initialize the Objects
                     */
                    AppContext.init(ChooserActivity.this, textView.getText().toString());

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
                settings.setBanner(R.color.colorAccent);
                AppContext.getCodemojoClient().launchReferralScreen(settings);
                break;
            case R.id.btnGamificationAchievements:
                startActivity(new Intent(this, GamificationActivity.class));
        }
    }
}