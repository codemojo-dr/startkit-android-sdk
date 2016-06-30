package io.codemojo.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import io.codemojo.sdk.exceptions.AuthenticationException;
import io.codemojo.sdk.facades.GamificationEarnedEvent;
import io.codemojo.sdk.facades.LoyaltyEvent;
import io.codemojo.sdk.services.AuthenticationService;
import io.codemojo.sdk.services.GamificationService;
import io.codemojo.sdk.services.LoyaltyService;
import io.codemojo.sdk.services.ReferralService;
import io.codemojo.sdk.services.WalletService;
import io.codemojo.sdk.ui.ReferralActivity;

/**
 * Created by shoaib on 24/06/16.
 */
public class Codemojo {

    private static AuthenticationService authenticationService;
    private LoyaltyService loyaltyService;
    private WalletService walletService;
    private GamificationService gamificationService;
    private ReferralService referralService;

    private GamificationEarnedEvent gamificationEarnedEvent;
    private LoyaltyEvent loyaltyEvent;

    private Context context;

    public Codemojo() {
    }

    public Codemojo(Context context, String client_token, String logged_in_user_id) {
        this(context, client_token, logged_in_user_id, false);
    }

    public Codemojo(Context context, String client_token, String logged_in_user_id, boolean testing) {
        this.context = context;
        try {
            authenticationService = new AuthenticationService(client_token, logged_in_user_id, testing ? 0 : 1);
            if(context instanceof Activity) {
                authenticationService.setContext((Activity) context);
            }
        } catch (AuthenticationException e) {
        }
    }

    public static AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public void launchReferralScreen(){
        context.startActivity(new Intent(context, ReferralActivity.class));
    }

    public void setLoyaltyEventListener(LoyaltyEvent loyaltyEvent) {
        this.loyaltyEvent = loyaltyEvent;
    }

    public void setGamificationEarnedEventListener(GamificationEarnedEvent gamificationEarnedEvent) {
        this.gamificationEarnedEvent = gamificationEarnedEvent;
    }

    public LoyaltyService getLoyaltyService() {
        if(loyaltyService == null){
            loyaltyService = new LoyaltyService(getAuthenticationService(), loyaltyEvent);
        }
        return loyaltyService;
    }

    public WalletService getWalletService() {
        if(walletService == null){
            walletService = new WalletService(getAuthenticationService());
        }
        return walletService;
    }

    public GamificationService getGamificationService() {
        if(gamificationService == null){
            gamificationService = new GamificationService(getAuthenticationService(), gamificationEarnedEvent);
        }
        return gamificationService;
    }

    public ReferralService getReferralService() {
        if(referralService == null) {
            referralService = new ReferralService(authenticationService);
        }
        return referralService;
    }
}
