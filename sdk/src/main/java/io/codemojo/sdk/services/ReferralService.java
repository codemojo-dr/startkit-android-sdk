package io.codemojo.sdk.services;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import io.codemojo.sdk.exceptions.AuthenticationException;
import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.ResourceNotFoundException;
import io.codemojo.sdk.exceptions.SDKInitializationException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.GenericResponse;
import io.codemojo.sdk.network.IReferral;
import io.codemojo.sdk.responses.ResponseReferralCode;
import io.codemojo.sdk.utils.APICodes;
import retrofit2.Call;

/**
 * Created by shoaib on 16/06/16.
 */
public class ReferralService extends BaseService {

    private final IReferral referralService;

    /**
     * @param authenticationService
     */
    public ReferralService(AuthenticationService authenticationService) {
        super(authenticationService, IReferral.class);
        referralService = (IReferral) getService();
    }

    /**
     * @param context
     * @param callback
     */
    public void useSignupReferral(Context context, final ResponseAvailable callback){
        final SharedPreferences preferences = context.getSharedPreferences("codemojo",Context.MODE_PRIVATE);
        final String referral = preferences.getString("referrer", null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(referral != null){
                    applyReferralCode(referral, new ResponseAvailable() {
                        @Override
                        public void available(final Object result) {
                            if(callback != null)
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        if((boolean) result == true){
                                            preferences.edit().remove("referral").apply();
                                        }
                                        callback.available(result);
                                    }
                                });
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * @param callback
     */
    public void getReferralCode(final ResponseAvailable callback) {
        if (referralService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<ResponseReferralCode> response = referralService.getReferralCode(getCustomerId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseReferralCode code = response.execute().body();
                    if(code != null){
                        switch (code.getCode()){
                            case APICodes.RESPONSE_SUCCESS:
                                if(callback != null)
                                    moveTo(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.available(code.getReferralCode());
                                        }
                                    });
                                break;
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(code.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(code.getMessage()));
                                break;
                            case APICodes.RESOURCE_NOT_FOUND:
                                raiseException(new ResourceNotFoundException(code.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(code.getMessage()));
                                break;
                        }
                    }
                } catch (Exception ignored) {
                    raiseException(ignored);
                    if(callback != null)
                        moveTo(new Runnable() {
                            @Override
                            public void run() {
                                callback.available(null);
                            }
                        });
                }
            }
        }).start();
    }

    /**
     * @param referral_code
     * @param callback
     */
    public void applyReferralCode(String referral_code, final ResponseAvailable callback) {
        if (referralService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<GenericResponse> response = referralService.applyReferralCode(getCustomerId(), referral_code);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final GenericResponse body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()){
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case APICodes.RESOURCE_NOT_FOUND:
                                raiseException(new ResourceNotFoundException(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                if(callback != null)
                                    moveTo(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.available(true);
                                        }
                                    });
                                break;
                        }
                    }
                } catch (IOException ignored) {
                    raiseException(ignored);
                    if(callback != null)
                        moveTo(new Runnable() {
                            @Override
                            public void run() {
                                callback.available(null);
                            }
                        });
                }
                if(callback != null)
                    moveTo(new Runnable() {
                        @Override
                        public void run() {
                            callback.available(false);
                        }
                    });
            }
        }).start();
    }

    /**
     * @param callback
     */
    public void markActionAsComplete(final ResponseAvailable callback) {
        if (referralService == null){
            raiseException(new SDKInitializationException());
            return;
        }
        final Call<GenericResponse> response = referralService.markActionComplete(getCustomerId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GenericResponse body = response.execute().body();
                    if(body != null){
                        switch (body.getCode()){
                            case APICodes.INVALID_MISSING_FIELDS:
                                raiseException(new InvalidArgumentsException(body.getMessage()));
                                break;
                            case APICodes.SERVICE_NOT_SETUP:
                                raiseException(new SetupIncompleteException(body.getMessage()));
                                break;
                            case APICodes.RESOURCE_NOT_FOUND:
                                raiseException(new ResourceNotFoundException(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_FAILURE:
                                raiseException(new Exception(body.getMessage()));
                                break;
                            case APICodes.RESPONSE_SUCCESS:
                                if(callback != null) moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(true);
                                    }
                                });
                                break;
                        }
                    } else {
                        if(callback != null)
                            moveTo(new Runnable() {
                                @Override
                                public void run() {
                                    callback.available(false);
                                }
                            });
                    }
                } catch (IOException ignored) {
                    raiseException(ignored);
                    if(callback != null)
                        moveTo(new Runnable() {
                            @Override
                            public void run() {
                                callback.available(false);
                            }
                        });
                }
            }
        }).start();
    }

}
