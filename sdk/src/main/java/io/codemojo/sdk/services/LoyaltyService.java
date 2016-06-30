package io.codemojo.sdk.services;

import java.io.IOException;
import java.util.Map;

import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.ResourceNotFoundException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.LoyaltyEvent;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.GamificationAchievement;
import io.codemojo.sdk.models.GamificationStatus;
import io.codemojo.sdk.models.GamificationSummary;
import io.codemojo.sdk.models.Loyalty;
import io.codemojo.sdk.models.LoyaltySummary;
import io.codemojo.sdk.responses.ResponseGamification;
import io.codemojo.sdk.responses.ResponseGamificationAchievement;
import io.codemojo.sdk.responses.ResponseGamificationSummary;
import io.codemojo.sdk.network.IGamification;
import io.codemojo.sdk.network.ILoyalty;
import io.codemojo.sdk.responses.ResponseLoyalty;
import io.codemojo.sdk.responses.ResponseLoyaltyMaximumRedemption;
import io.codemojo.sdk.responses.ResponseLoyaltySummary;
import retrofit2.Call;

/**
 * Created by shoaib on 16/06/16.
 */
public class LoyaltyService extends BaseService {

    private final ILoyalty loyaltyService;
    private LoyaltyEvent notification;

    /**
     * @param authenticationService
     * @param notification
     */
    public LoyaltyService(AuthenticationService authenticationService, LoyaltyEvent notification) {
        super(authenticationService, ILoyalty.class);
        this.notification = notification;
        loyaltyService = (ILoyalty) getService();
    }

    /**
     * @param customer_id
     * @param transaction
     * @param transaction_id
     * @param meta
     * @param tag
     * @param platform
     * @param service
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     * @throws ResourceNotFoundException
     */
    public void addLoyaltyPoints(String customer_id, float transaction, String transaction_id, String meta, String tag, String platform, String service, final ResponseAvailable callback) {
        final Call<ResponseLoyalty> response = loyaltyService.addLoyaltyPoints(customer_id, transaction, transaction_id, meta, tag, platform, service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseLoyalty body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case -403:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                            case 200:
                                if(notification != null && body.getResult().isTierUpgrade()) {
                                    notification.newTierUpgrade(body.getResult().getTier());
                                }
                                callback.available(body.getResult());
                        }
                    }
                } catch (Exception ignored) {
                    raiseException(ignored);
                }

            }
        }).start();
    }

    /**
     * @param customer_id
     * @param transaction
     * @param transaction_id
     * @param meta
     * @param tag
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     * @throws ResourceNotFoundException
     */
    public void addLoyaltyPoints(String customer_id, float transaction, String transaction_id, String meta, String tag, ResponseAvailable callback) throws InvalidArgumentsException, SetupIncompleteException {
        addLoyaltyPoints(customer_id, transaction, transaction_id, meta, tag, null, null, callback);
    }

    public void addLoyaltyPoints(String customer_id, float transaction, String transaction_id, ResponseAvailable callback) throws InvalidArgumentsException, SetupIncompleteException {
        addLoyaltyPoints(customer_id, transaction, transaction_id, null, null, null, null, callback);
    }

    /**
     * @param customer_id
     * @param transaction
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     */
    public void calculateLoyaltyPoints(String customer_id, float transaction, ResponseAvailable callback) {
        calculateLoyaltyPoints(customer_id, transaction, null, null, callback);
    }

    /**
     * @param customer_id
     * @param transaction
     * @param platform
     * @param service
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     */
    public void calculateLoyaltyPoints(String customer_id, float transaction, String platform, String service, final ResponseAvailable callback) {
        final Call<ResponseLoyalty> response = loyaltyService.calculateLoyaltyPoints(customer_id, transaction, platform, service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseLoyalty body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case -403:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                            case 200:
                                callback.available(body.getResult().getAward());
                        }
                    }
                } catch (Exception ignored) {
                    raiseException(ignored);
                }
            }
        }).start();
    }

    /**
     * @param customer_id
     * @param transaction
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     */
    public void maximumRedemption(String customer_id, float transaction, ResponseAvailable callback) throws InvalidArgumentsException, SetupIncompleteException {
        maximumRedemption(customer_id, transaction, callback);
    }

    /**
     * @param customer_id
     * @param transaction
     * @param platform
     * @param service
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     * @throws ResourceNotFoundException
     */
    public void maximumRedemption(String customer_id, float transaction, String platform, String service, final ResponseAvailable callback) throws InvalidArgumentsException, SetupIncompleteException, ResourceNotFoundException {
        final Call<ResponseLoyaltyMaximumRedemption> response = loyaltyService.maximumRedemption(customer_id, transaction, platform, service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseLoyaltyMaximumRedemption body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case -403:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                            case 200:
                                callback.available(body.getResult());
                        }
                    }
                } catch (Exception ignored) {
                    raiseException(ignored);
                }
            }
        }).start();
    }

    /**
     * @return
     * @throws InvalidArgumentsException
     * @throws SetupIncompleteException
     */
    public void getSummary(final ResponseAvailable callback) throws InvalidArgumentsException, SetupIncompleteException {
        final Call<ResponseLoyaltySummary> response = loyaltyService.summary(getCustomerId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseLoyaltySummary body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case -403:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                            case 400:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                            case 200:
                                callback.available(body.getSummary());
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }).start();
    }
}
