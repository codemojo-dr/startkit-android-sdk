package io.codemojo.sdk.facades;

import java.util.Map;

import io.codemojo.sdk.models.GamificationAchievement;

/**
 * Created by shoaib on 24/06/16.
 */
public interface GamificationEarnedEvent {

    void newBadgeUnlocked(int totalPoints, String badgeName);

    void newAchievementUnlocked(int totalAchievements, String achievementName, GamificationAchievement achievement);

    void updatedAchievemenstAvailable(Map<String, GamificationAchievement> achievements);
}
