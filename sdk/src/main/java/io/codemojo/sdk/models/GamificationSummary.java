package io.codemojo.sdk.models;

import java.util.Map;

/**
 * Created by shoaib on 26/10/14.
 */
public class GamificationSummary extends GamificationStatus {

    private int total_points;
    private Map<String, Integer> statistics;
    private GamificationAchievement achievements;

    public int getTotalPoints() {
        return total_points;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    public GamificationAchievement getAchievements() {
        return achievements;
    }
}
