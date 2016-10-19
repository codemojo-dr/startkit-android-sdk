package io.codemojo.sdk.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by shoaib on 28/07/16.
 */
public class GCMInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

}
