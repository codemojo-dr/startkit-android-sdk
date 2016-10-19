package io.codemojo.sdk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import io.codemojo.sdk.exceptions.AuthenticationException;
import io.codemojo.sdk.facades.GamificationEarnedEvent;
import io.codemojo.sdk.facades.LoyaltyEvent;
import io.codemojo.sdk.gcm.RegistrationIntentService;
import io.codemojo.sdk.models.ReferralScreenSettings;
import io.codemojo.sdk.services.AuthenticationService;
import io.codemojo.sdk.services.GamificationService;
import io.codemojo.sdk.services.LoyaltyService;
import io.codemojo.sdk.services.MessagingService;
import io.codemojo.sdk.services.ReferralService;
import io.codemojo.sdk.services.UserDataSyncService;
import io.codemojo.sdk.services.WalletService;
import io.codemojo.sdk.ui.GamificationTransactions;
import io.codemojo.sdk.ui.ReferralActivity;
import io.codemojo.sdk.utils.GCMChecker;

/**
 * Created by shoaib on 24/06/16.
 */
public class Codemojo {

    private static AuthenticationService authenticationService;
    private LoyaltyService loyaltyService;
    private WalletService walletService;
    private GamificationService gamificationService;
    private ReferralService referralService;
    private UserDataSyncService dataSyncService;
    private static MessagingService messagingService;

    private GamificationEarnedEvent gamificationEarnedEvent;
    private LoyaltyEvent loyaltyEvent;

    private Context context;

    /**
     * @param context
     * @param client_token
     * @param logged_in_user_id
     */
    public Codemojo(Context context, String client_token, String logged_in_user_id) {
        this(context, client_token, logged_in_user_id, false);
    }

    /**
     * @param context
     * @param client_token
     * @param hashed_user_id
     * @param testing
     */
    public Codemojo(Context context, String client_token, String hashed_user_id, boolean testing) {
        this.context = context;
        try {
            authenticationService = new AuthenticationService(client_token, hashed_user_id, testing ? 0 : 1);
            if(context instanceof Activity) {
                authenticationService.setContext((Activity) context);
            }
        } catch (AuthenticationException e) {
            System.out.println(e);
        }
    }

    public static AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    /**
     * @param settings
     */
    public void launchReferralScreen(ReferralScreenSettings settings){
        Intent referralIntent = new Intent(context, ReferralActivity.class);
        referralIntent.putExtra("settings", settings);
        context.startActivity(referralIntent);
    }

    public void launchGamificationTransactionScreen(){
        Intent gamificationTransactionIntent = new Intent(context, GamificationTransactions.class);
        context.startActivity(gamificationTransactionIntent);
    }

    /**
     * @param loyaltyEvent
     */
    public void setLoyaltyEventListener(LoyaltyEvent loyaltyEvent) {
        this.loyaltyEvent = loyaltyEvent;
    }

    /**
     * @param gamificationEarnedEvent
     */
    public void setGamificationEarnedEventListener(GamificationEarnedEvent gamificationEarnedEvent) {
        this.gamificationEarnedEvent = gamificationEarnedEvent;
    }

    /**
     * @return
     */
    public LoyaltyService getLoyaltyService() {
        if(loyaltyService == null){
            loyaltyService = new LoyaltyService(getAuthenticationService(), loyaltyEvent);
        }
        return loyaltyService;
    }

    /**
     * @return
     */
    public WalletService getWalletService() {
        if(walletService == null){
            walletService = new WalletService(getAuthenticationService());
        }
        return walletService;
    }

    /**
     * @return
     */
    public GamificationService getGamificationService() {
        if(gamificationService == null){
            gamificationService = new GamificationService(getAuthenticationService(), gamificationEarnedEvent);
        }
        return gamificationService;
    }

    /**
     * @return
     */
    public ReferralService getReferralService() {
        if(referralService == null) {
            referralService = new ReferralService(authenticationService);
        }
        return referralService;
    }

    public UserDataSyncService getUserDataSyncService() {
        return dataSyncService;
    }

    public static MessagingService getMessagingService(Context context){
        if(messagingService == null) {
            messagingService = new MessagingService(context);
        }
        return messagingService;
    }

    public void enableGCM(){
        if(GCMChecker.checkPlayServices((Activity) context)){
            Intent gcmRegistration = new Intent(context, RegistrationIntentService.class);
            context.startService(gcmRegistration);
            context.bindService(gcmRegistration, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RegistrationIntentService.RegistrationBinder service = (RegistrationIntentService.RegistrationBinder) iBinder;
            service.getService().sendRegistrationToServer(authenticationService);
            context.unbindService(serviceConnection);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
