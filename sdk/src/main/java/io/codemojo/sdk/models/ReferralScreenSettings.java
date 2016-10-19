package io.codemojo.sdk.models;

import java.io.Serializable;

/**
 * Created by shoaib on 01/07/16.
 */
public class ReferralScreenSettings implements Serializable {

    private static final String DEFAULT_MESSAGE = "Hey! I was trying out this app {app} and I found it pretty interesting.\n\n" +
            "I would like you to try it out as well {url}";
    private String referralDesription = "Your friend will get Rs.{friend} and you will get Rs.{you} after their first transaction";

    private String message;
    private String urlVariable = "{url}";
    private String appNameVariable = "{app}";
    private String referralCodeVariable = "{code}";
    private String youVariable = "{you}";
    private String friendVariable = "{friend}";
    private String subjectLine = "Try out the app {app}";
    private String pageTite = "Invite Friends";
    private String appName;
    private String callToActionTitle = "Invite Friends";
    private boolean allowPromoEnter = false;
    private int banner;
    private ReferralCode code;

    public ReferralScreenSettings(String appName) {
        this.appName = appName;
    }

    public void setReferralObject(ReferralCode referral) {
        this.code = referral;
    }

    public int getBanner() {
        return banner;
    }

    public void setBanner(int banner) {
        this.banner = banner;
    }

    public String getReferralCodeVariable() {
        return referralCodeVariable;
    }

    public void setReferralDesription(String referralDesription) {
        this.referralDesription = referralDesription;
    }

    public String getReferralDesription() {
        String desc = referralDesription.replace("{you}", String.valueOf(code.getReward().getYou()));
        desc = desc.replace("{friend}", String.valueOf(code.getReward().getFriend()));
        return desc;
    }

    public String getYouVariable() {
        return youVariable;
    }


    public String getFriendVariable() {
        return friendVariable;
    }

    public String getUrlVariable() {
        return urlVariable;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppNameVariable() {
        return appNameVariable;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void allowManualPromoCode(boolean allowPromoEnter) {
        this.allowPromoEnter = allowPromoEnter;
    }

    public String getMessage() {
        if(message == null || message.trim().equals("") || !message.contains(getUrlVariable())){
            message = DEFAULT_MESSAGE;
        }
        String tmp = message;
        tmp = tmp.replace(getAppNameVariable(), getAppName());
        tmp = tmp.replace(getUrlVariable(), code.getUrl());
        tmp = tmp.replace(getReferralCodeVariable(), code.getCode());
        return tmp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubjectLine() {
        String tmp = subjectLine.replace(getAppNameVariable(), getAppName());
        return tmp;
    }

    public void setSubjectLine(String subjectLine) {
        this.subjectLine = subjectLine;
    }

    public String getPageTite() {
        return pageTite;
    }

    public void setPageTite(String pageTite) {
        this.pageTite = pageTite;
    }

    public String getCallToActionTitle() {
        return callToActionTitle;
    }

    public void setCallToActionTitle(String callToActionTitle) {
        this.callToActionTitle = callToActionTitle;
    }
}
