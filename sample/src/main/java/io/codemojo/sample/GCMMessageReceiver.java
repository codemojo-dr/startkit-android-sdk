package io.codemojo.sample;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.exceptions.UnknownMessageType;
import io.codemojo.sdk.services.MessagingService;

/**
 * Created by shoaib on 14/08/16.
 */
public class GCMMessageReceiver extends GcmListenerService {

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        super.onMessageReceived(s, bundle);
        MessagingService messagingService = Codemojo.getMessagingService(getApplicationContext());
        if(messagingService.isCodemojoMessage(bundle)) {
            try {
                messagingService.processMessage(bundle);
            } catch (UnknownMessageType unknownMessageType) {
                unknownMessageType.printStackTrace();
            }
        } else {
            // Process your regular GCM
        }
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

}
