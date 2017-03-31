package io.codemojo.sdk.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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

public class AvailableRewardsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listTransactions;
    private RewardsAdapter adapter;
    private RewardsScreenSettings settings;
    private boolean clickProcessing = false;
    private RewardsFlowReceiver receiver = new RewardsFlowReceiver();

    private int session_clock = 0;
    private Thread clockThread;
    private final Object clockLock = new Object();
    private List<BrandReward> rewardsList;

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(getString(R.string.intent_rewards_ui)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = (RewardsScreenSettings) getIntent().getSerializableExtra("settings");

        if(settings.shouldAnimateScreenLoad()) {
            overridePendingTransition(R.anim.up_from_bottom, 0);
        }

        setContentView(R.layout.activity_available_rewards);

        findViewById(R.id.codemojo_dialog_rewards_close_button).setOnClickListener(this);
        TextView btnClose = (TextView) findViewById(R.id.codemojo_dialog_rewards_close_button);

        final RewardsService rewardsService = Codemojo.getRewardsService();

        listTransactions = (ListView) findViewById(R.id.lstTransactions);

        listTransactions.setOnItemClickListener(this);

        TextView description = ((TextView) findViewById(R.id.codemojo_dialog_rewards_title));
        description.setOnClickListener(this);
        if(settings.getRewardsSelectionPageTitle() == null) {
            description.setVisibility(View.GONE);
        } else {
            if (!settings.getRewardsSelectionPageTitle().isEmpty()) {
                description.setText(Html.fromHtml(settings.getRewardsSelectionPageTitle()));
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
            btnClose.setTextColor(getApplicationContext().getResources().getColor(settings.getThemeAccentFontColor()));
        }

        if(settings.getThemeTitleStripeColor() > 0){
            description.setBackgroundColor(getApplicationContext().getResources().getColor(settings.getThemeTitleStripeColor()));
            btnClose.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(settings.getThemeTitleStripeColor()));
        }

        try {
            if(getSupportActionBar() != null) getSupportActionBar().hide();
        } catch (Exception e) {
            Log.e("Log", e.getMessage());
        }

        if(settings.isShouldShowCloseButton()) {
            btnClose.setOnClickListener(this);
        } else {
            btnClose.setVisibility(View.GONE);
        }

        rewardsList = null;

        try {
            rewardsList = (ArrayList<BrandReward>) getIntent().getSerializableExtra("rewards_list");
        } catch (Exception e) {
        }

        if(rewardsList == null) {
            Map<String, String> filters = new HashMap<>();
            if(settings.getLocale() != null){
                filters.put("locale", settings.getLocale());
            }
            if(settings.getLatitude() != 0) filters.put("lat", String.valueOf(settings.getLatitude()));
            if(settings.getLongitude() != 0) filters.put("lon", String.valueOf(settings.getLongitude()));
            if(!settings.getCommunicationChannel().isEmpty()) filters.put("email", settings.getCommunicationChannel());
            settings.setCommunicationChannel("email_id");
            settings.setWaitForUserInput(false);
            if(rewardsService != null) {
                rewardsService.getAvailableRewards(null, filters, new ResponseAvailable() {
                    @Override
                    public void available(Object result) {
                        if (result != null) {
                            rewardsList = (List<BrandReward>) result;
                            updateAdapterWithRewards((List<BrandReward>) result);
                        }
                    }
                });
            }
        } else {
            updateAdapterWithRewards(rewardsList);
        }

        clockThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    session_clock++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        clockThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(clockThread != null) try {
            synchronized (clockLock) {
                clockThread.wait();
            }
        } catch (InterruptedException | IllegalMonitorStateException e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(clockThread != null) {
            try {
                synchronized (clockLock) {
                    clockThread.notify();
                }
            } catch (IllegalMonitorStateException e) {
            }
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
        if (clickProcessing) return;

        clickProcessing = true;

        BrandReward reward = (BrandReward) adapterView.getAdapter().getItem(i);
        Intent details = new Intent(this, RewardDetailsActivity.class);
        details.putExtra("reward", reward);
        details.putExtra("settings", settings);
        /*
         * Check if a user defined handler is set
         */
        if(settings.getRewardSelectListener() != null){
            /*
             * If the event is consumed, stop further processing
             */
            details.setAction(Codemojo.ON_REWARD_SELECT);
            if(settings.getRewardSelectListener().onClick(details, this)){
                clickProcessing = false;
                return;
            }
        }
        startActivityForResult(details, Codemojo.CODEMOJO_REWARD_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Codemojo.CODEMOJO_REWARD_USER){
            if(resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK, data);

                if(Codemojo.getRewardsCallbackListener() != null){
                    Codemojo.getRewardsCallbackListener().grabbed(data);
                }

                finish();
            } else if(resultCode == Activity.RESULT_CANCELED) {
                setResult(Activity.RESULT_CANCELED, data);
                if(data.hasExtra("error")){
                    if(Codemojo.getRewardsCallbackListener() != null) {
                        Codemojo.getRewardsCallbackListener().error(data.getStringExtra("error"));
                    }
                    if(Codemojo.getRewardsErrorListener() != null){
                        Codemojo.getRewardsErrorListener().onError(data.getStringExtra("error"));
                    }
                }
                finish();
            }
        }
        clickProcessing = false;
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        if(settings != null && settings.shouldAnimateScreenLoad()) {
            overridePendingTransition(0, R.anim.hide_from_top);
        }

        /*
         * Log the session time
         */
        Map<String, String> detail = new HashMap<>();
        List<String> ids = new ArrayList<>();

        if(rewardsList != null) {
            for (BrandReward r : rewardsList) {
                ids.add(r.getId());
            }

            if (settings != null && !settings.getCommunicationChannel().isEmpty()) {
                detail.put("email", settings.getCommunicationChannel());
            }
            Codemojo.getRewardsService().clockSession(ids, session_clock, detail);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.codemojo_dialog_rewards_title) {

            /*
            * Check if a user defined handler is set
            */
            if (settings.getTitleClickListener() != null) {
                Intent intent = new Intent();
                intent.setAction(Codemojo.ON_VIEW_MILESTONE_CLICK);
                if(settings.getTitleClickListener().onClick(intent, this)) {
                    return;
                }
            }

            if(settings.getMileStones() != null) {
                Intent mileStonesActivity = new Intent(this, RewardsMilestonesActivity.class);
                mileStonesActivity.putExtra("settings", settings);
                startActivity(mileStonesActivity);
            }
        } else if( view.getId() == R.id.codemojo_dialog_rewards_close_button) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        }
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
