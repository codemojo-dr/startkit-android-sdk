package io.codemojo.sdk.services;

import java.io.IOException;

import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.ResourceNotFoundException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.GamificationEarnedEvent;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.network.IGamification;
import io.codemojo.sdk.responses.ResponseGamification;
import io.codemojo.sdk.responses.ResponseGamificationAchievement;
import io.codemojo.sdk.responses.ResponseGamificationSummary;
import io.codemojo.sdk.responses.ResponseWalletTransaction;
import retrofit2.Call;

/**
 * Created by shoaib on 16/06/16.
 */
public class GamificationService extends BaseService {

    private final IGamification gamificationService;
    private GamificationEarnedEvent notification;
    private int transactionPage = 0;

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
     * @param callback
     */
    public void captureAction(String action_id, final ResponseAvailable callback) {
        final Call<ResponseGamification> response = gamificationService.addAction(getCustomerId(), action_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseGamification body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case -403:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                                break;
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case 505:
                            case 404:
                            case -405:
                                raiseException(new ResourceNotFoundException(body.getMessage()));
                                break;
                            case 200:
                                if(notification != null && body.getGamificationStatus().isBadgeUpgrade()) {
                                    notification.newBadgeUnlocked(body.getGamificationStatus().getCurrentPoints(), body.getGamificationStatus().getBadge());
                                }
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(body.getGamificationStatus());
                                    }
                                });
                                break;
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
     */
    public void captureAchievementsAction(String action_id, ResponseAvailable callback)  {
        captureAchievementsAction(action_id, null, callback);
    }

    /**
     * @param action_id
     * @param category_id
     */
    public void captureAchievementsAction(String action_id, String category_id, final ResponseAvailable callback) {
        final Call<ResponseGamificationAchievement> response = gamificationService.addAchievement(getCustomerId(), action_id, category_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseGamificationAchievement body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case -403:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                                break;
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case 404:
                            case 505:
                            case -405:
                                raiseException(new ResourceNotFoundException(body.getMessage()));
                                break;
                            case 200:
                                if(callback != null){
                                    moveTo(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.available(body.getAchievements());
                                        }
                                    });
                                }
                                if(notification != null) {
                                    for (String achievement : body.getAchievements().keySet()) {
                                        if (body.getAchievements().get(achievement).isNewBagdeEarned()) {
                                            notification.newAchievementUnlocked(body.getAchievements().get(achievement).getTotal(), achievement, body.getAchievements().get(achievement));
                                        }
                                    }
                                }
                                notification.updatedAchievemenstAvailable(body.getAchievements());
                                break;
                            default:
                                if(callback != null){
                                    moveTo(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.available(false);
                                        }
                                    });
                                }
                                break;
                        }
                    }
                } catch (IOException e) {
                    raiseException(e);
                }
            }
        }).start();
    }

    /**
     * @param callback
     */
    public void getUserSummary(final ResponseAvailable callback) {
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
                                break;
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case 200:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(body.getSummary());
                                    }
                                });
                                break;
                        }
                    }
                } catch (IOException ignored) {
                    raiseException(ignored);
                }
            }
        }).start();
    }

    public GamificationService getGamificationTransactions(int count, final ResponseAvailable callback){
        return nextTransaction(count, callback);
    }

    private GamificationService getGamificationTransactions(int count, final ResponseAvailable callback, int page){
        final Call<ResponseWalletTransaction> response = gamificationService.getTransaction(getCustomerId(), count, 3, page);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseWalletTransaction code  = response.execute().body();
                    if(code != null){
                        switch (code.getCode()){
                            case -403:
                                raiseException(new InvalidArgumentsException(code.getMessage()));
                                break;
                            case 400:
                                raiseException(new SetupIncompleteException(code.getMessage()));
                                break;
                            case 200:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(code.getTransactions());
                                    }
                                });
                                break;
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
        return this;
    }

    public void reset(){
        transactionPage = 1;
    }

    public GamificationService prevTransaction(int count, ResponseAvailable callback){
        transactionPage--;
        if(transactionPage <= 0){
            transactionPage = 1;
        }
        return getGamificationTransactions(count, callback, transactionPage);
    }

    public GamificationService nextTransaction(int count, ResponseAvailable callback){
        transactionPage++;
        return getGamificationTransactions(count, callback, transactionPage);
    }
}
