package io.codemojo.sdk.models;

import java.io.Serializable;

/**
 * Created by shoaib on 16/06/16.
 */
public class BrandReward implements Serializable {

    private String id;
    private String brand_name;
    private String logo;
    private String offer;
    private String fineprint;
    private String redemption_process;
    private String support;
    private String value_formatted;
    private int value_numeric;
    private float value_ratio;
    private long valid_till;

    public String getId() {
        return id;
    }

    public String getBrandName() {
        return brand_name;
    }

    public String getLogo() {
        return logo;
    }

    public String getOffer() {
        return offer;
    }

    public String getValueFormatted() {
        return value_formatted;
    }

    public int getValueNumeric() {
        return value_numeric;
    }

    public float getValueRatio() {
        return value_ratio;
    }

    public long getValidTill() {
        return valid_till;
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
}
