package io.codemojo.sdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.R;
import io.codemojo.sdk.adapters.RewardsAdapter;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.BrandReward;
import io.codemojo.sdk.models.RewardsScreenSettings;
import io.codemojo.sdk.services.RewardsService;

public class AvailableRewardsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listTransactions;
    private RewardsAdapter adapter;
    private RewardsScreenSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = (RewardsScreenSettings) getIntent().getSerializableExtra("settings");

        if(settings.shouldAnimateScreenLoad()) {
            overridePendingTransition(R.anim.up_from_bottom, 0);
        }

        setContentView(R.layout.activity_available_rewards);

        final RewardsService rewardsService = Codemojo.getRewardsService();

        listTransactions = (ListView) findViewById(R.id.lstTransactions);

        listTransactions.setOnItemClickListener(this);

        TextView description = ((TextView) findViewById(R.id.lblDescription));
        if(settings.getRewardsSelectionPageTitle() == null) {
            description.setVisibility(View.GONE);
        } else {
            if (!settings.getRewardsSelectionPageTitle().isEmpty()) {
                description.setText(settings.getRewardsSelectionPageTitle());
            } else {
                if (settings.isAllowGrab()) {
                    description.setText("Congratulations! Please pick a reward");
                } else {
                    description.setText("Unlock these on completing your milestone!");
                }
            }
        }

        if(settings.getThemeTitleColor() > 0){
            description.setTextColor(getApplicationContext().getResources().getColor(settings.getThemeTitleColor()));
        }

        if(settings.getThemeTitleStripeColor() > 0){
            description.setBackgroundColor(getApplicationContext().getResources().getColor(settings.getThemeTitleStripeColor()));
        }

        try {
            if(getSupportActionBar() != null) getSupportActionBar().hide();
        } catch (Exception e) {
            Log.e("Log", e.getMessage());
        }

        ArrayList<BrandReward> rewardsList = null;

        try {
            rewardsList = (ArrayList<BrandReward>) getIntent().getSerializableExtra("rewards_list");
        } catch (Exception e) {
        }

        if(rewardsList == null) {
            Map<String, String> filters = new HashMap<>();
            if(settings.getLocale() != null) filters.put("locale", settings.getLocale());
            if(settings.getLatitude() != 0) filters.put("lat", String.valueOf(settings.getLatitude()));
            if(settings.getLongitude() != 0) filters.put("lon", String.valueOf(settings.getLongitude()));
            if(!settings.getCommunicationChannel().isEmpty()) filters.put("email", settings.getCommunicationChannel());
            rewardsService.getAvailableRewards(null, filters, new ResponseAvailable() {
                @Override
                public void available(Object result) {
                    if (result != null) {
                        updateAdapterWithRewards((List<BrandReward>) result);
                    }
                }
            });
        } else {
            updateAdapterWithRewards(rewardsList);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAdapterWithRewards(List<BrandReward> rewardsList) {
        adapter = new RewardsAdapter(AvailableRewardsActivity.this,
                android.R.layout.simple_list_item_1, rewardsList);
        listTransactions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BrandReward reward = (BrandReward) adapterView.getAdapter().getItem(i);
        Intent details = new Intent(this, RewardDetailsActivity.class);
        details.putExtra("reward", reward);
        details.putExtra("settings", settings);
        startActivityForResult(details, Codemojo.CODEMOJO_REWARD_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Codemojo.CODEMOJO_REWARD_USER){
            if(resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK, data);
                finish();
            } else if(resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, data);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        if(settings.shouldAnimateScreenLoad()) {
            overridePendingTransition(0, R.anim.hide_from_top);
        }
    }
}
