package io.codemojo.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChooserActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        final EditText text = (EditText) findViewById(R.id.editClientID);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(!textView.getText().toString().trim().equals("")){
                    AppContext.init(ChooserActivity.this, textView.getText().toString());
                    text.setVisibility(View.GONE);
                    findViewById(R.id.panelDemoChooser).setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(ChooserActivity.this, "Please type any username/email", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        findViewById(R.id.btnGamification).setOnClickListener(this);
        findViewById(R.id.btnReferral).setOnClickListener(this);

        getSupportActionBar().setTitle("Codemojo Demo");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReferral:
                AppContext.getCodemojoClient().launchReferralScreen();
                break;
            case R.id.btnGamification:
                startActivity(new Intent(this, GamificationActivity.class));
        }
    }
}
