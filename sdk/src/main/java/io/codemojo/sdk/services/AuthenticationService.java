package io.codemojo.sdk.services;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import io.codemojo.sdk.exceptions.AuthenticationException;
import io.codemojo.sdk.models.OAuth;
import io.codemojo.sdk.network.IAccessToken;
import io.codemojo.sdk.utils.Constants;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shoaib on 16/06/16.
 */
public class AuthenticationService extends UIThread {

    private Call<OAuth> response;
    IAccessToken oAuthService;
    protected OAuth token;
    private String customer_id;

    private int environment = 1;

    private Semaphore synchronizer = new Semaphore(0);
    /**
     * Authenticate the device with the User context
     * @param app_token Token from Codemojo dashboard
     * @param customer_id Unique ID of the user signed-in
     * @throws AuthenticationException
     */
    public AuthenticationService(final String app_token, String customer_id, int environment) throws AuthenticationException {
        this.customer_id = customer_id;
        this.environment = environment;

        oAuthService = new Retrofit.Builder().baseUrl(Constants.getEndpoint(environment)).addConverterFactory(GsonConverterFactory.create()).build().create(IAccessToken.class);
        response = oAuthService.getAppSecret(app_token);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    token = response.execute().body();
                } catch (IOException e) {
                }
                synchronizer.release();
            }
        }).start();

        try {
            synchronizer.acquire();
            synchronizer = null;
        } catch (InterruptedException e) {
        }
        if(token == null){
            throw new AuthenticationException("error in authentication");
        }
    }

    public int getEnvironment() {
        return environment;
    }

    protected String getAccessToken(){
        try {
            if(synchronizer != null)
                synchronizer.acquire();
        } catch (InterruptedException e) {
        }
        return token == null? "": token.getAccessToken();
    }

    protected String getCustomerId() {
        return customer_id;
    }
}
