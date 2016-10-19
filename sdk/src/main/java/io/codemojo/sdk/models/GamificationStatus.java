package io.codemojo.sdk.models;

/**
 * Created by shoaib on 26/10/14.
 */
public class GamificationStatus {

    private int points_added;
    private int current_points;
    private String badge;
    private boolean badge_upgrade;
    private long expires;
    public long getExpires() {
        return expires;
    }

    public int getPointsAdded() {
        return points_added;
    }

    public int getCurrentPoints() {
        return current_points;
    }

    public String getBadge() {
        return badge;
    }

    public boolean isBadgeUpgrade() {
        return badge_upgrade;
    }
}
