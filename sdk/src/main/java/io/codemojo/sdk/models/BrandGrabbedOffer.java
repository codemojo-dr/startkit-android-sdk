package io.codemojo.sdk.models;

import java.io.Serializable;

/**
 * Created by shoaib on 16/06/16.
 */
public class BrandGrabbedOffer implements Serializable {

    private String coupon_code;
    private String title;
    private String fineprint;
    private String redemption_process;
    private String support;
    private String sms_message;
    private String email_message;

    public String getCouponCode() {
        return coupon_code;
    }

    public String getTitle() {
        return title;
    }

    public String getFineprint() {
        return fineprint;
    }

    public String getRedemptionProcess() {
        return redemption_process;
    }

    public String getSupport() {
        return support;
    }

    public String getSmsMessage() {
        return sms_message;
    }

    public String getEmailMessage() {
        return email_message;
    }
}
