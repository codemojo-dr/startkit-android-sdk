package io.codemojo.sdk.responses;

import java.util.Map;

import io.codemojo.sdk.models.GamificationAchievement;
import io.codemojo.sdk.models.GamificationStatus;
import io.codemojo.sdk.models.GenericResponse;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseGamificationAchievement extends GenericResponse {

    private Map<String, GamificationAchievement> results;

    private int total_points;

    private GamificationStatus badges;

    public GamificationStatus getBadges() {
        return badges;
    }

    public int getTotalPoints() {
        return total_points;
    }

    public Map<String, GamificationAchievement> getAchievements() {
        return results;
    }
}
