package io.codemojo.sdk.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import io.codemojo.sdk.facades.CodemojoException;
import io.codemojo.sdk.services.AuthenticationService;
import io.codemojo.sdk.services.UserDataSyncService;

/**
 * Created by shoaib on 28/07/16.
 */
public class RegistrationIntentService extends IntentService {

    private static final String[] TOPICS = {"global"};
    private AuthenticationService authenticationService;
    private String token;

    public RegistrationIntentService(){
        super("RegistrationService");
    }
    public RegistrationIntentService(String name) {
        super(name);
    }

    // Binder given to clients
    private final IBinder mBinder = new RegistrationBinder();


    public String getToken() {
        return token;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class RegistrationBinder extends Binder {
        public RegistrationIntentService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RegistrationIntentService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        InstanceID instanceID = InstanceID.getInstance(this);

        authenticationService = (AuthenticationService) intent.getSerializableExtra("auth");

        try {
            token = instanceID.getToken("778940712972", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            return;
        }

        sharedPreferences.edit().putString("cm_device_id", token).apply();


        // Subscribe to topic channels
        try {
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickStartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (IOException e) {
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickStartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }


    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     */
    public void sendRegistrationToServer(AuthenticationService authenticationService) {
        // Add custom implementation, as needed.
        UserDataSyncService service = new UserDataSyncService(authenticationService);
        service.setErrorHandler(new CodemojoException() {
            @Override
            public void onError(Exception exception) {
                Log.e("GCM", exception.getMessage());
            }
        });

        service.addDevice(0, token, null);

    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]
}
