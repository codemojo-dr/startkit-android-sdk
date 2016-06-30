package io.codemojo.sdk.models;

/**
 * Created by shoaib on 26/10/14.
 */
public class GamificationAchievement {
    private int total;
    private long last_achieved;
    private String label;
    private boolean new_bagde_earned;

    public int getTotal() {
        return total;
    }

    public long getLastAchieved() {
        return last_achieved;
    }

    public boolean isNewBagdeEarned() {
        return new_bagde_earned;
    }

    public String getLabel() {
        return label;
    }
}
