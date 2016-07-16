package io.codemojo.sdk.models;

/**
 * Created by shoaib on 26/10/14.
 */
public class ReferralCode {

    private String url;
    private String code;
    private ReferralReward reward;

    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;
    }

    public ReferralReward getReward() {
        return reward;
    }
}
