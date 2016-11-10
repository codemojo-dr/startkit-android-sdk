package io.codemojo.sdk.services;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.SDKInitializationException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.facades.RewardsAvailability;
import io.codemojo.sdk.models.BrandReward;
import io.codemojo.sdk.models.ResponseAvailableRewards;
import io.codemojo.sdk.models.ResponseRewardGrab;
import io.codemojo.sdk.network.IRewards;
import io.codemojo.sdk.utils.APICodes;
import retrofit2.Call;

public class RewardsService extends BaseService {

    private final IRewards rewardsService;
    private String app_id;

    /**
     * @param authenticationService @description Authentication Service
     */
    public RewardsService(AuthenticationService authenticationService, String app_id) {
        super(authenticationService, IRewards.class);
        rewardsService = (IRewards) getService();
        this.app_id = app_id;
    }

    /**
     * @param user_communication_id @description User Email or Mobile Number for Communication
     * @param callback @description Async Callback
     */
    public void onRewardsAvailable(String user_communication_id, final RewardsAvailability callback) {

        getAvailableRewards(user_communication_id, null, new ResponseAvailable() {
            @Override
            public void available(Object result) {
                if(callback == null){
                    return;
                }
                if (result != null) {
                    callback.available((List<BrandReward>) result);
                } else {
                    callback.unavailable();
                }
            }
        });

        callback.processing();

    }

    /**
     * @param user_communication_id @description User Email or Mobile Number for Communication
     * @param countryCode @description 2 digit Country code
     * @param callback @description Async Callback
     */
    public void onRewardsAvailable(String user_communication_id, String countryCode, final RewardsAvailability callback) {

        Map<String, String> filter = new HashMap<>();
        filter.put("locale", countryCode);

        getAvailableRewards(user_communication_id, filter, new ResponseAvailable() {
            @Override
            public void available(Object result) {
                if (result != null) {
                    callback.available((List<BrandReward>) result);
                } else {
                    callback.unavailable();
                }
            }
        });
    }

    /**
     * @param user_communication_id @description User Email or Mobile Number for Communication
     * @param lat @description User location
     * @param lon @description User location
     * @param callback @description Async Callback
     */
    public void onRewardsAvailable(String user_communication_id, double lat, double lon, final RewardsAvailability callback) {

        Map<String, String> filter = new HashMap<>();
        filter.put("lat", String.valueOf(lat));
        filter.put("lon", String.valueOf(lon));

        getAvailableRewards(user_communication_id, filter, new ResponseAvailable() {
            @Override
            public void available(Object result) {
                if (result != null) {
                    callback.available((List<BrandReward>) result);
                } else {
                    callback.unavailable();
                }
            }
        });
    }

    /**
     * @param communication_channel @description User Email or Mobile Number for Communication
     * @param callback @description Async Callback
     */
    public void getAvailableRewards(String communication_channel, final ResponseAvailable callback) {

        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String locale = tm.getSimCountryIso();

        Map<String, String> filter = new HashMap<>();
        filter.put("locale", locale);

        getAvailableRewards(communication_channel, filter, callback);
    }

    /**
     * @param communication_channel @description User Email or Mobile Number for Communication
     * @param filters @description Filters
     * @param callback @description Async Callback
     */
    private void getAvailableRewards(String communication_channel, Map<String, String> filters, final ResponseAvailable callback) {
        if (rewardsService == null) {
            raiseException(new SDKInitializationException());
            return;
        }

        if( filters == null ){
            filters = new HashMap<>();
        }

        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String locale = tm.getSimCountryIso();

        if(!filters.containsKey("locale")) {
            filters.put("locale", locale);
        }


        Location location = getBestAvailableLocation();
        if(location != null && !filters.containsKey("lat") && !filters.containsKey("lon")){
            filters.put("lat", String.valueOf(location.getLatitude()));
            filters.put("lon", String.valueOf(location.getLongitude()));
        }

        if(communication_channel != null) {
            filters.put("email", communication_channel);
            filters.put("phone", communication_channel);
        }

        final Map<String, String> finalFilters = filters;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    try {
                        String device_id = AdvertisingIdClient.getAdvertisingIdInfo(getContext()).getId();
                        if (!finalFilters.containsKey("device_id")) {
                            finalFilters.put("device_id", device_id);
                        }
                    } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    }

                    final Call<ResponseAvailableRewards> response = rewardsService.getAvailableRewards(
                            app_id, getCustomerId(), finalFilters
                    );

                    final ResponseAvailableRewards body = response.execute().body();
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
                                        callback.available(body.getRewards());
                                    }
                                });
                                break;
                            default:
                                raiseException(new Exception(body.getMessage()));
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
     * @param reward_id @description Reward ID to be grabbed
     * @param additional_details @description Additional details
     * @param callback @description Async Callback
     */
    public void grabReward(final String reward_id, String communication_channel, Map<String, String> additional_details , final ResponseAvailable callback) {
        if (rewardsService == null){
            raiseException(new SDKInitializationException());
            return;
        }

        if(additional_details == null){
            additional_details = new HashMap<>();
        }

        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String locale = tm.getSimCountryIso();

        if(!additional_details.containsKey("locale")) {
            additional_details.put("locale", locale);
        }

        Location location = getBestAvailableLocation();
        if(location != null){
            additional_details.put("lat", String.valueOf(location.getLatitude()));
            additional_details.put("lon", String.valueOf(location.getLongitude()));
        }

        if(communication_channel != null) {
            additional_details.put("email", communication_channel);
            additional_details.put("phone", communication_channel);
        }

        final Map<String, String> finalAdditional_details = additional_details;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    try {
                        String device_id = AdvertisingIdClient.getAdvertisingIdInfo(getContext()).getId();
                        if (!finalAdditional_details.containsKey("device_id")) {
                            finalAdditional_details.put("device_id", device_id);
                        }
                    } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    }

                    final Call<ResponseRewardGrab> response = rewardsService.grabReward(
                            app_id, reward_id, getCustomerId(), finalAdditional_details
                    );

                    final ResponseRewardGrab body = response.execute().body();
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
                                        callback.available(body.getOffer());
                                    }
                                });
                                break;
                            default:
                                raiseException(new Exception(body.getMessage()));
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
     * @param reward_id @description Reward ID to be grabbed
     * @param communication_channel @description User Email or Mobile Number for Communication
     * @param callback @description Async Callback
     */
    public void grabReward(final String reward_id, String communication_channel, final ResponseAvailable callback) {
        grabReward(reward_id, communication_channel, null, callback);
    }

    /**
     * @return
     */
    private Location getBestAvailableLocation(){
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = locationManager.getAllProviders();
            Location location = null;
            for (String provider :
                    providers) {
                location = locationManager.getLastKnownLocation(provider);
                if(location != null){
                    return location;
                }
            }
        }
        return null;
    }
}
