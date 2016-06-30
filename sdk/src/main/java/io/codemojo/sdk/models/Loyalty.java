package io.codemojo.sdk.models;

/**
 * Created by shoaib on 16/06/16.
 */
public class Loyalty {

    private String id;
    private String tier;
    private float award;
    private boolean tier_upgrade;

    public String getId() {
        return id;
    }

    public float getAward() {
        return award;
    }

    public boolean isTierUpgrade() {
        return tier_upgrade;
    }

    public String getTier() {
        return tier;
    }
}
