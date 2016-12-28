package io.codemojo.sdk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import io.codemojo.sdk.exceptions.AuthenticationException;
import io.codemojo.sdk.facades.GamificationEarnedEvent;
import io.codemojo.sdk.facades.LoyaltyEvent;
import io.codemojo.sdk.facades.RewardsAvailability;
import io.codemojo.sdk.facades.RewardsDialogListener;
import io.codemojo.sdk.models.BrandReward;
import io.codemojo.sdk.models.ReferralScreenSettings;
import io.codemojo.sdk.models.RewardsScreenSettings;
import io.codemojo.sdk.services.AuthenticationService;
import io.codemojo.sdk.services.GamificationService;
import io.codemojo.sdk.services.LoyaltyService;
import io.codemojo.sdk.services.MessagingService;
import io.codemojo.sdk.services.ReferralService;
import io.codemojo.sdk.services.RewardsService;
import io.codemojo.sdk.services.UserDataSyncService;
import io.codemojo.sdk.services.WalletService;
import io.codemojo.sdk.ui.AvailableRewardsActivity;
import io.codemojo.sdk.ui.GamificationTransactions;
import io.codemojo.sdk.ui.ReferralActivity;

/**
 * Created by shoaib on 24/06/16.
 */
public class Codemojo {

    public static final int CODEMOJO_REWARD_USER = 0x1A;
    public static final int CODEMOJO_REFERRAL = 0x1B;

    public static final String ON_REWARD_SELECT = "codemojo_reward_select";
    public static final String ON_REWARD_GRAB_CLICK = "codemojo_reward_grab_click";
    public static final String ON_VIEW_MILESTONE_CLICK = "codemojo_view_milestone_click";

    private static AuthenticationService authenticationService;

    private LoyaltyService loyaltyService;
    private static RewardsService rewardsService;
    private WalletService walletService;
    private GamificationService gamificationService;
    private ReferralService referralService;
    private UserDataSyncService dataSyncService;
    private static MessagingService messagingService;

    private GamificationEarnedEvent gamificationEarnedEvent;
    private LoyaltyEvent loyaltyEvent;

    private Activity context;
    private static String appId;
    private static RewardsDialogListener titleClickListener;
    private static RewardsDialogListener viewMilestoneListener;
    private static RewardsDialogListener rewardSelectListener;
    private static RewardsDialogListener rewardGrabListener;
    private static RewardsAvailability rewardsCallbackListener;


    /**
     * @param context
     * @param client_token
     * @param logged_in_user_id
     */
    public Codemojo(Activity context, String client_token, String logged_in_user_id) {
        this(context, client_token, logged_in_user_id, false);
    }

    /**
     * @param context
     * @param client_token
     * @param hashed_user_id
     * @param testing
     */
    public Codemojo(Activity context, String client_token, String hashed_user_id, boolean testing) {
        this.context = context;
        try {
            authenticationService = new AuthenticationService(client_token, hashed_user_id, testing ? 0 : 1);
            if(context instanceof Activity) {
                authenticationService.setContext(context);
            }
        } catch (AuthenticationException e) {
            System.out.println(e);
        }
    }

    public static AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public static void setAppId(String appId) {
        Codemojo.appId = appId;
    }

    public static String getAppId() {
        return appId;
    }

    public static void setTitleClickListener(RewardsDialogListener titleClickListener) {
        Codemojo.titleClickListener = titleClickListener;
    }

    public static RewardsDialogListener getTitleClickListener() {
        return titleClickListener;
    }

    public static void setViewMilestoneListener(RewardsDialogListener viewMilestoneListener) {
        Codemojo.viewMilestoneListener = viewMilestoneListener;
    }

    public static RewardsDialogListener getViewMilestoneListener() {
        return viewMilestoneListener;
    }

    public static void setRewardSelectListener(RewardsDialogListener rewardSelectListener) {
        Codemojo.rewardSelectListener = rewardSelectListener;
    }

    public static RewardsDialogListener getRewardSelectListener() {
        return rewardSelectListener;
    }

    public static void setRewardGrabListener(RewardsDialogListener rewardGrabListener) {
        Codemojo.rewardGrabListener = rewardGrabListener;
    }

    public static RewardsDialogListener getRewardGrabListener() {
        return rewardGrabListener;
    }

    /**
     * @param settings
     */
    public void launchReferralScreen(ReferralScreenSettings settings, Activity callback){
        Intent referralIntent = new Intent(context, ReferralActivity.class);
        referralIntent.putExtra("settings", settings);
        if(callback == null){
            context.startActivity(referralIntent);
        } else {
            context.startActivityForResult(referralIntent, CODEMOJO_REFERRAL);
        }
    }

    public void launchReferralScreen(ReferralScreenSettings settings){
        launchReferralScreen(settings, null);
    }

    public void launchGamificationTransactionScreen(){
        Intent gamificationTransactionIntent = new Intent(context, GamificationTransactions.class);
        context.startActivity(gamificationTransactionIntent);
    }


    public void closeRewardsScreen(){
        Intent intent = new Intent();
        intent.setAction("io.codemojo.sdk.rewards_ui");
        intent.putExtra("exit_flow", true);
        context.sendBroadcast(intent);
    }

    public void launchAvailableRewardsScreen(List<BrandReward> rewardList, RewardsScreenSettings settings, Activity resultPostBack){
        Intent availableRewardsIntent = new Intent(context, AvailableRewardsActivity.class);
        if(rewardList != null){
            availableRewardsIntent.putExtra("rewards_list", new ArrayList<>(rewardList));
        }
        availableRewardsIntent.putExtra("settings", settings);
        if(resultPostBack == null) {
            context.startActivity(availableRewardsIntent);
        } else {
            resultPostBack.startActivityForResult(availableRewardsIntent, CODEMOJO_REWARD_USER);
        }
    }

    public void launchAvailableRewardsScreen(List<BrandReward> rewardList, RewardsScreenSettings settings, RewardsAvailability resultPostBack) {
        Intent availableRewardsIntent = new Intent(context, AvailableRewardsActivity.class);
        if(rewardList != null){
            availableRewardsIntent.putExtra("rewards_list", new ArrayList<>(rewardList));
        }
        settings.setRewardsCallbackListener(resultPostBack);
        availableRewardsIntent.putExtra("settings", settings);
        context.startActivity(availableRewardsIntent);
    }

    public void launchAvailableRewardsScreen(RewardsScreenSettings settings){
        launchAvailableRewardsScreen(null, settings, (RewardsAvailability) null);
    }

    public void launchAvailableRewardsScreen(RewardsScreenSettings settings, Activity resultPostBack){
        launchAvailableRewardsScreen(null, settings, resultPostBack);
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

    public RewardsService initRewardsService(String app_id) {
        if (rewardsService == null) {
            rewardsService = new RewardsService(authenticationService, app_id);
        }
        Codemojo.setAppId(app_id);
        return rewardsService;
    }

    public static RewardsService getRewardsService(){
        return rewardsService;
    }

    public static RewardsAvailability getRewardsCallbackListener() {
        return rewardsCallbackListener;
    }

    public static void setRewardsCallbackListener(RewardsAvailability rewardsCallbackListener) {
        Codemojo.rewardsCallbackListener = rewardsCallbackListener;
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

}
