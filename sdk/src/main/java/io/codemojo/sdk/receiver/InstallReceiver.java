package io.codemojo.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by shoaib on 24/06/16.
 */
public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String referrerString = (String) extras.get("referrer");
        SharedPreferences preferences = context.getSharedPreferences("codemojo", Context.MODE_PRIVATE);

        if(!preferences.contains("referrer") && referrerString != null) {
            // Parse the query string, extracting the relevant data
            preferences.edit().putString("referrer", referrerString).apply();
        }
    }
}
