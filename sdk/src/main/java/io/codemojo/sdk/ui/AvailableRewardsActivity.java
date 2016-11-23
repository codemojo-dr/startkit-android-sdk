package io.codemojo.sdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        setContentView(R.layout.activity_available_rewards);

        final RewardsService rewardsService = Codemojo.getRewardsService();

        listTransactions = (ListView) findViewById(R.id.lstTransactions);

        listTransactions.setOnItemClickListener(this);
        settings = (RewardsScreenSettings) getIntent().getSerializableExtra("settings");

        if(!settings.getRewardsSelectionPageDescription().isEmpty()){
            ((TextView) findViewById(R.id.lblDescription)).setText(settings.getRewardsSelectionPageDescription());
        } else{
            if(settings.isAllowGrab()) {
                ((TextView) findViewById(R.id.lblDescription)).setText("Congratulations on your Achievement, Please pick a reward from the available options below");
            } else {
                ((TextView) findViewById(R.id.lblDescription)).setText("You can pick any voucher from below on completing your milestone!");
            }
        }

        if(settings.getThemePrimaryColor() > 0) {
            getSupportActionBar().setBackgroundDrawable(
                    getApplicationContext().getResources().getDrawable(settings.getThemePrimaryColor())
            );
        }

        if(settings.getThemeSecondaryColor() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getApplicationContext().getResources().getColor(settings.getThemeSecondaryColor()));
            }
        }

        // Title
        getSupportActionBar().setTitle(settings.getRewardsSelectionPageTitle().equals("") ?
                getResources().getString(R.string.rewards_selection_title): settings.getRewardsSelectionPageTitle());

        // Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(settings.isShowBackButtonOnTitleBar());


        ArrayList<BrandReward> rewardsList = null;

        try {
            rewardsList = (ArrayList<BrandReward>) getIntent().getSerializableExtra("rewards_list");
        } catch (Exception e) {
        }

        if(rewardsList == null) {
            Map<String, String> filters = new HashMap<>();
            filters.put("locale", settings.getLocale());
            filters.put("lat", String.valueOf(settings.getLatitude()));
            filters.put("lon", String.valueOf(settings.getLongitude()));
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
}
