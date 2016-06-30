package io.codemojo.sdk.services;

import android.util.Log;

import java.io.IOException;

import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.ResourceNotFoundException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.GamificationEarnedEvent;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.responses.ResponseGamification;
import io.codemojo.sdk.responses.ResponseGamificationAchievement;
import io.codemojo.sdk.responses.ResponseGamificationSummary;
import io.codemojo.sdk.network.IGamification;
import retrofit2.Call;

/**
 * Created by shoaib on 16/06/16.
 */
public class GamificationService extends BaseService {

    private final IGamification gamificationService;
    private GamificationEarnedEvent notification;

    /**
     * @param authenticationService
     * @param notification
     */
    public GamificationService(AuthenticationService authenticationService, GamificationEarnedEvent notification) {
        super(authenticationService, IGamification.class);
        this.notification = notification;
        gamificationService = (IGamification) getService();
    }

    /**
     * @param action_id
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     * @throws ResourceNotFoundException
     */
    public void captureAction(String action_id, final ResponseAvailable callback) throws InvalidArgumentsException, SetupIncompleteException, ResourceNotFoundException {
        final Call<ResponseGamification> response = gamificationService.addAction(getCustomerId(), action_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseGamification body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case -403:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                            case 404:
                            case -405:
                                raiseException(new ResourceNotFoundException(body.getMessage()));
                            case 200:
                                if(notification != null && body.getGamificationStatus().isBadgeUpgrade()) {
                                    notification.newBadgeUnlocked(body.getGamificationStatus().getCurrentPoints(), body.getGamificationStatus().getBadge());
                                }
                                callback.available(body.getGamificationStatus());
                        }
                    }
                } catch (Exception ignored) {
                    raiseException(ignored);
                }
            }
        }).start();
    }

    /**
     * @param action_id
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     * @throws ResourceNotFoundException
     */
    public void captureAchievementsAction(String action_id)  {
        captureAchievementsAction(action_id, null);
    }

    /**
     * @param action_id
     * @param category_id
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     * @throws ResourceNotFoundException
     */
    public void captureAchievementsAction(String action_id, String category_id) {
        final Call<ResponseGamificationAchievement> response = gamificationService.addAchievement(getCustomerId(), action_id, category_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ResponseGamificationAchievement body = null;
                try {
                    body = response.execute().body();
                } catch (IOException e) {
                    raiseException(e);
                }
                if(body != null){
                    switch (body.getCode()) {
                        case -403:
                            raiseException(new InvalidArgumentsException(body.getMessage()));
                        case 400:
                            raiseException(new SetupIncompleteException(body.getMessage()));
                        case 404:
                        case -405:
                            raiseException(new ResourceNotFoundException(body.getMessage()));
                        case 200:
                            if(notification != null) {
                                for (String achievement : body.getAchievements().keySet()) {
                                    if (body.getAchievements().get(achievement).isNewBagdeEarned()) {
                                        notification.newAchievementUnlocked(body.getAchievements().get(achievement).getTotal(), achievement, body.getAchievements().get(achievement));
                                    }
                                }
                            }
                            notification.updatedAchievemenstAvailable(body.getAchievements());
                    }
                }
            }
        }).start();
    }

    /**
     * @return
     * @throws SetupIncompleteException
     * @throws InvalidArgumentsException
     */
    public void getUserSummary(final ResponseAvailable callback) throws SetupIncompleteException, InvalidArgumentsException {
        final Call<ResponseGamificationSummary> response = gamificationService.getSummary(getCustomerId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseGamificationSummary body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case -403:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                            case 200:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                callback.available(body.getSummary());
                                    }
                                });
                        }
                    }
                } catch (IOException ignored) {
                    raiseException(ignored);
                }
            }
        }).start();
    }

}
