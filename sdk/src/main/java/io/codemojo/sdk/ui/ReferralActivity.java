package io.codemojo.sdk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.R;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.ReferralCode;
import io.codemojo.sdk.models.WalletBalance;
import io.codemojo.sdk.services.ReferralService;
import io.codemojo.sdk.services.WalletService;

public class ReferralActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnInvite;
    private ReferralCode referral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);

        ReferralService referralService = new ReferralService(Codemojo.getAuthenticationService());

        getSupportActionBar().setTitle("Invite Friends");

        btnInvite = (Button) findViewById(R.id.btnInvite);
        btnInvite.setOnClickListener(this);
        btnInvite.setEnabled(false);

        referralService.getReferralCode(new ResponseAvailable() {
            @Override
            public void available(Object result) {
                btnInvite.setEnabled(true);
                referral = ((ReferralCode)result);
                ((TextView) findViewById(R.id.lblReferralCode)).setText(referral.getCode());
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey there, I'm using this app. Join me and get 50% off on first order.\n\nClick here to signup " + referral.getUrl());
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Join me in Codemojo");
        startActivity(Intent.createChooser(shareIntent, "Invite via"));
    }
}
