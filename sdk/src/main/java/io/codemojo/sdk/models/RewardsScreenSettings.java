package io.codemojo.sdk.models;

import java.io.Serializable;
import java.util.ArrayList;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.facades.RewardsDialogListener;

public class RewardsScreenSettings implements Serializable {

    private String rewardsSelectionPageTitle;
    private String communicationChannel;

    private int themeTitleColor;
    private int themeTitleStripeColor;
    private int themeButtonColor;
    private int themeAccentFontColor;

    private boolean allowGrab = true;
    private boolean sendCouponAutomatically = true;
    private boolean test = false;
    private boolean waitForUserInput = true;
    private boolean animate = false;

    private String locale;
    private double latitude;
    private double longitude;

    private ArrayList<Milestone> milesStones;

    public ArrayList<Milestone> getMilesStones() {
        return milesStones;
    }

    public void setMilesStones(ArrayList<Milestone> milesStones) {
        this.milesStones = milesStones;
    }

    public String getRewardsSelectionPageTitle() {
        return rewardsSelectionPageTitle == null? "": rewardsSelectionPageTitle.trim();
    }

    public boolean isAllowGrab() {
        return allowGrab;
    }

    public boolean isSendCouponAutomatically() {
        return sendCouponAutomatically;
    }

    public void setRewardsSelectionPageTitle(String rewardsSelectionPageTitle) {
        this.rewardsSelectionPageTitle = rewardsSelectionPageTitle;
    }

    public int getThemeButtonColor() {
        return themeButtonColor;
    }

    public void setThemeButtonColor(int themeButtonColor) {
        this.themeButtonColor = themeButtonColor;
    }

    public void setAllowGrab(boolean allowGrab) {
        this.allowGrab = allowGrab;
    }

    public void setSendCouponAutomatically(boolean sendCouponAutomatically) {
        this.sendCouponAutomatically = sendCouponAutomatically;
    }

    public boolean isTest() {
        return test;
    }

    public void setTesting(boolean test) {
        this.test = test;
    }

    public void setThemeAccentFontColor(int themeAccentFontColor) {
        this.themeAccentFontColor = themeAccentFontColor;
    }

    public int getThemeAccentFontColor() {
        return themeAccentFontColor;
    }

    public String getCommunicationChannel() {
        return communicationChannel == null?"":communicationChannel.trim();
    }

    public void setCommunicationChannel(String communicationChannel) {
        this.communicationChannel = communicationChannel;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getThemeTitleColor() {
        return themeTitleColor;
    }

    public void setThemeTitleColor(int themeTitleColor) {
        this.themeTitleColor = themeTitleColor;
    }

    public int getThemeTitleStripeColor() {
        return themeTitleStripeColor;
    }

    public void setThemeTitleStripeColor(int themeTitleStripeColor) {
        this.themeTitleStripeColor = themeTitleStripeColor;
    }

    public boolean shouldWaitForUserInput() {
        return waitForUserInput;
    }

    public void setWaitForUserInput(boolean waitForUserInput) {
        this.waitForUserInput = waitForUserInput;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public boolean shouldAnimateScreenLoad() {
        return animate;
    }

    public void setAnimateScreenLoad(boolean animate) {
        this.animate = animate;
    }

    public void setTitleClickListener(RewardsDialogListener listener){
        Codemojo.setTitleClickListener(listener);
    }

    public void setViewMilestoneClickListener(RewardsDialogListener listener){
        Codemojo.setViewMilestoneListener(listener);
    }

    public void setRewardSelectListener(RewardsDialogListener rewardSelectListener) {
        Codemojo.setRewardSelectListener(rewardSelectListener);
    }

    public void setRewardGrabListener(RewardsDialogListener rewardSelectListener) {
        Codemojo.setRewardGrabListener(rewardSelectListener);
    }

    public RewardsDialogListener getTitleClickListener() {
        return Codemojo.getTitleClickListener();
    }

    public RewardsDialogListener getViewMilestoneListener() {
        return Codemojo.getViewMilestoneListener();
    }

    public RewardsDialogListener getRewardSelectListener() {
        return Codemojo.getRewardSelectListener();
    }

    public RewardsDialogListener getRewardGrabListener() {
        return Codemojo.getRewardGrabListener();
    }

}
