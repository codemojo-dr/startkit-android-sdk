package io.codemojo.sdk.services;

import java.io.IOException;

import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.SDKInitializationException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.GenericResponse;
import io.codemojo.sdk.network.IUserSync;
import io.codemojo.sdk.utils.APICodes;
import retrofit2.Call;

/**
 * Created by shoaib on 16/06/16.
 */
public class UserDataSyncService extends BaseService {

    private final IUserSync userSyncService;

    /**
     * @param authenticationService
     */
    public UserDataSyncService(AuthenticationService authenticationService) {
        super(authenticationService, IUserSync.class);
        userSyncService = (IUserSync) getService();
    }

    /**
     * @param callback
     */
    public void addDevice(int device_type, String device_id, final ResponseAvailable callback) {
        if (userSyncService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<GenericResponse> response = userSyncService.addDevice(getCustomerId(), device_type, device_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final GenericResponse code  = response.execute().body();
                    if(code != null){
                        switch (code.getCode()){
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(code.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(code.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(code.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(callback != null) {
                                            callback.available(code.getCode() == 200);
                                        }
                                    }
                                });
                                break;
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
    }

    /**
     * @param callback
     */
    public void addMeta(String name, Object value, final ResponseAvailable callback) {
        if (userSyncService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<GenericResponse> response = userSyncService.addMeta(getCustomerId(), name, value);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final GenericResponse code  = response.execute().body();
                    if(code != null){
                        switch (code.getCode()){
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(code.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(code.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(code.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(callback != null) {
                                            callback.available(code.getCode() == 200);
                                        }
                                    }
                                });
                                break;
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
    }

    /**
     * @param callback
     */
    public void addEvent(String event_name, final ResponseAvailable callback) {
        if (userSyncService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<GenericResponse> response = userSyncService.addEvent(getCustomerId(), event_name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final GenericResponse code  = response.execute().body();
                    if(code != null){
                        switch (code.getCode()){
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(code.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(code.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(code.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(callback != null) {
                                            callback.available(code.getCode() == 200);
                                        }
                                    }
                                });
                                break;
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
    }



}
