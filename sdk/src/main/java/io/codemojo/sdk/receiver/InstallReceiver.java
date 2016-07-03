package io.codemojo.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by shoaib on 24/06/16.
 */
public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String referrerString = extras.getString("referrer");
        Toast.makeText(context, "Referral ID used " + referrerString, Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = context.getSharedPreferences("codemojo", Context.MODE_PRIVATE);
        if(referrerString != null) {
            if(referrerString.contains("referrer=")) {
                referrerString = referrerString.replace("referrer=", "");
            }
            preferences.edit().putString("referrer", referrerString).apply();
        }
    }
}
