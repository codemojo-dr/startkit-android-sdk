package io.codemojo.sdk.responses;

import java.util.Map;

import io.codemojo.sdk.models.GamificationAchievement;
import io.codemojo.sdk.models.GenericResponse;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseGamificationAchievement extends GenericResponse {

    private Map<String, GamificationAchievement> results;

    public Map<String, GamificationAchievement> getAchievements() {
        return results;
    }
}
