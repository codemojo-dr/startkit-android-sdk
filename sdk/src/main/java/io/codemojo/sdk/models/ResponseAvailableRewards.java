package io.codemojo.sdk.models;

import java.util.List;

/**
 * Created by shoaib on 16/06/16.
 */
public class ResponseAvailableRewards {

    private int code;
    private List<BrandReward> results;
    private String message;
    private String status;

    private int count;

    public int getCode() {
        return code;
    }

    public List<BrandReward> getRewards() {
        return results;
    }

    public int getCount() {
        return count;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
