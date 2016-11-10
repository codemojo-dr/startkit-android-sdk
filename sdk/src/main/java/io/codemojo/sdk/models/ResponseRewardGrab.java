package io.codemojo.sdk.models;

/**
 * Created by shoaib on 16/06/16.
 */
public class ResponseRewardGrab {

    private int code;
    private BrandGrabbedOffer offer;
    private String message;
    private String status;

    public int getCode() {
        return code;
    }

    public BrandGrabbedOffer getOffer() {
        return offer;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
