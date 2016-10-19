package io.codemojo.sdk.services;

import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.SDKInitializationException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.LoyaltyEvent;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.network.ILoyalty;
import io.codemojo.sdk.responses.ResponseLoyalty;
import io.codemojo.sdk.responses.ResponseLoyaltyMaximumRedemption;
import io.codemojo.sdk.responses.ResponseLoyaltySummary;
import io.codemojo.sdk.utils.APICodes;
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
     * @param callback
     */
    public void addLoyaltyPoints(String customer_id, float transaction, String transaction_id, String meta, String tag, String platform, String service, final ResponseAvailable callback) {
        if (loyaltyService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<ResponseLoyalty> response = loyaltyService.addLoyaltyPoints(customer_id, transaction, transaction_id, meta, tag, platform, service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseLoyalty body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                if(notification != null && body.getResult().isTierUpgrade()) {
                                    notification.newTierUpgrade(body.getResult().getTier());
                                }
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(body.getResult());
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
     * @param customer_id
     * @param transaction
     * @param transaction_id
     * @param meta
     * @param tag
     * @param callback
     */
    public void addLoyaltyPoints(String customer_id, float transaction, String transaction_id, String meta, String tag, ResponseAvailable callback) {
        addLoyaltyPoints(customer_id, transaction, transaction_id, meta, tag, null, null, callback);
    }

    /**
     * @param customer_id
     * @param transaction
     * @param transaction_id
     * @param callback
     */
    public void addLoyaltyPoints(String customer_id, float transaction, String transaction_id, ResponseAvailable callback) {
        addLoyaltyPoints(customer_id, transaction, transaction_id, null, null, null, null, callback);
    }

    /**
     * @param customer_id
     * @param transaction
     * @param callback
     */
    public void calculateLoyaltyPoints(String customer_id, float transaction, ResponseAvailable callback) {
        calculateLoyaltyPoints(customer_id, transaction, null, null, callback);
    }

    /**
     * @param customer_id
     * @param transaction
     * @param platform
     * @param service
     * @param callback
     */
    public void calculateLoyaltyPoints(String customer_id, float transaction, String platform, String service, final ResponseAvailable callback) {
        if (loyaltyService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<ResponseLoyalty> response = loyaltyService.calculateLoyaltyPoints(customer_id, transaction, platform, service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseLoyalty body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(body.getResult().getAward());
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
     * @param customer_id
     * @param transaction
     * @param callback
     */
    public void maximumRedemption(String customer_id, float transaction, ResponseAvailable callback) {
        maximumRedemption(customer_id, transaction, callback);
    }


    /**
     * @param customer_id
     * @param transaction
     * @param platform
     * @param service
     * @param callback
     */
    public void maximumRedemption(String customer_id, float transaction, String platform, String service, final ResponseAvailable callback) {
        if (loyaltyService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<ResponseLoyaltyMaximumRedemption> response = loyaltyService.maximumRedemption(customer_id, transaction, platform, service);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseLoyaltyMaximumRedemption body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(body.getResult());
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
     * @param callback
     */
    public void getSummary(final ResponseAvailable callback) {
        if (loyaltyService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<ResponseLoyaltySummary> response = loyaltyService.summary(getCustomerId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseLoyaltySummary body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()) {
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(body.getSummary());
                                    }
                                });
                                break;
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }).start();
    }
}
