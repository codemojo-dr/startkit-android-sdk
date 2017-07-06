package io.codemojo.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by shoaib on 24/06/16.
 */
public class InstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == intent || null == intent.getExtras()) {
            Log.e("CM_DD", "onReceive: intent or intent extras found null");
            return;
        }
        Bundle extras = intent.getExtras();
        String referrerString = (String) extras.get("referrer");

        Set<String> keys = extras.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            // Using {@String#format()} since proguard cannot strip string concatenation
            Log.e("CM_DD", String.format("[%s=%s]", key, extras.get(key)));
        }

        SharedPreferences preferences = context.getSharedPreferences("codemojo", Context.MODE_PRIVATE);
        Log.e("CM_REFER_2", referrerString);
        preferences.edit().putString("referrer", referrerString).apply();
    }
}