package io.codemojo.sdk.models;

import java.io.Serializable;

public class RewardsScreenSettings implements Serializable {

    private String rewardsSelectionPageTitle;
    private String rewardsSelectionPageDescription;
    private String rewardDetailsPageTitle;
    private String communicationChannel;

    private int themePrimaryColor;
    private int themeSecondaryColor;
    private int themeAccentColor;

    private boolean allowGrab = true;
    private boolean showBackButtonOnTitleBar = true;
    private boolean sendCouponAutomatically = true;
    private boolean test = false;
    private int themeAccentFontColor;

    public String getRewardsSelectionPageTitle() {
        return rewardsSelectionPageTitle == null? "": rewardsSelectionPageTitle.trim();
    }

    public String getRewardDetailsPageTitle() {
        return rewardDetailsPageTitle == null? "": rewardDetailsPageTitle.trim();
    }

    public int getThemePrimaryColor() {
        return themePrimaryColor;
    }

    public int getThemeSecondaryColor() {
        return themeSecondaryColor;
    }

    public boolean isAllowGrab() {
        return allowGrab;
    }

    public boolean isShowBackButtonOnTitleBar() {
        return showBackButtonOnTitleBar;
    }

    public boolean isSendCouponAutomatically() {
        return sendCouponAutomatically;
    }

    public void setRewardsSelectionPageTitle(String rewardsSelectionPageTitle) {
        this.rewardsSelectionPageTitle = rewardsSelectionPageTitle;
    }

    public void setRewardDetailsPageTitle(String rewardDetailsPageTitle) {
        this.rewardDetailsPageTitle = rewardDetailsPageTitle;
    }

    public void setThemePrimaryColor(int themePrimaryColor) {
        this.themePrimaryColor = themePrimaryColor;
    }

    public void setThemeSecondaryColor(int themeSecondaryColor) {
        this.themeSecondaryColor = themeSecondaryColor;
    }

    public int getThemeAccentColor() {
        return themeAccentColor;
    }

    public void setThemeAccentColor(int themeAccentColor) {
        this.themeAccentColor = themeAccentColor;
    }

    public void setAllowGrab(boolean allowGrab) {
        this.allowGrab = allowGrab;
    }

    public void setShowBackButtonOnTitleBar(boolean showBackButtonOnTitleBar) {
        this.showBackButtonOnTitleBar = showBackButtonOnTitleBar;
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

    public String getRewardsSelectionPageDescription() {
        return rewardsSelectionPageDescription == null?"":rewardsSelectionPageDescription.trim();
    }

    public void setRewardsSelectionPageDescription(String rewardsSelectionPageDescription) {
        this.rewardsSelectionPageDescription = rewardsSelectionPageDescription;
    }
}
