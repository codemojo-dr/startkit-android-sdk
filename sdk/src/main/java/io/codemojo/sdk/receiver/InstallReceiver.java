package io.codemojo.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shoaib on 24/06/16.
 */
public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String referrerString = extras.getString("referrer");
        SharedPreferences preferences = context.getSharedPreferences("codemojo", Context.MODE_PRIVATE);
        if(referrerString != null) {
            // Parse the query string, extracting the relevant data
            String[] params = referrerString.split("&"); // $NON-NLS-1$
            Map<String, String> referralParams = new HashMap<String, String>();

            for (String param : params)
            {
                String[] pair = param.split("="); // $NON-NLS-1$
                referralParams.put(pair[0], pair[1]);
            }
            if(referralParams.containsKey("referrer")) {
                referrerString = referralParams.get("referrer");
            }
            preferences.edit().putString("referrer", referrerString).apply();
        }
    }
}
