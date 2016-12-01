package io.codemojo.sdk.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import io.codemojo.sdk.R;
import io.codemojo.sdk.adapters.MileStonesAdapter;
import io.codemojo.sdk.models.Milestone;
import io.codemojo.sdk.models.RewardsScreenSettings;

public class RewardsMilestonesActivity extends AppCompatActivity {

    private ListView listMilestones;
    private RewardsScreenSettings settings;

    private RewardsFlowReceiver receiver = new RewardsFlowReceiver();

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(getString(R.string.intent_rewards_ui)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = (RewardsScreenSettings) getIntent().getSerializableExtra("settings");

        if(settings.shouldAnimateScreenLoad()) {
            overridePendingTransition(R.anim.up_from_bottom, 0);
        }

        setContentView(R.layout.activity_milestones);

        listMilestones = (ListView) findViewById(R.id.lstTransactions);

        TextView description = ((TextView) findViewById(R.id.codemojo_dialog_milestones_title));
        description.setText("Milestones");

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

        updateAdapterWithRewards(settings.getMilesStones());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAdapterWithRewards(List<Milestone> milestoneList) {
        MileStonesAdapter adapter = new MileStonesAdapter(this, android.R.layout.simple_list_item_1, milestoneList);
        listMilestones.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public class RewardsFlowReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(getString(R.string.intent_rewards_ui)) && intent.getBooleanExtra("exit_flow", false)){
                finish();
            }
        }
    }

}
