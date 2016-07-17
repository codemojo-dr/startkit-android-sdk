package io.codemojo.sdk.services;

import io.codemojo.sdk.facades.CodemojoException;
import io.codemojo.sdk.utils.AuthenticationInterceptor;
import io.codemojo.sdk.utils.Constants;
import io.codemojo.sdk.utils.LoggingInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shoaib on 24/06/16.
 */
public abstract class BaseService extends UIThread {

    private final String customer_id;
    private final Object service;
    private CodemojoException exception;

    public BaseService(AuthenticationService authenticationService, Class serviceClass) {
        if (authenticationService == null){
            service = customer_id = null;
            raiseException(new Exception("Cannot authenticate"));
            return;
        }
        setContext(authenticationService.getContext());
        this.customer_id = authenticationService.getCustomerId();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(authenticationService.getAccessToken()))
                .build();

        service = new Retrofit.Builder().baseUrl(Constants.getEndpoint(authenticationService.getEnvironment()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(serviceClass);
    }

    public void setErrorHandler(CodemojoException handler){
        this.exception = handler;
    }

    protected String getCustomerId() {
        return customer_id;
    }

    protected Object getService() {
        return service;
    }

    protected void raiseException(final Exception e){
        if(this.exception == null){
            return;
        }
        moveTo(new Runnable() {
            @Override
            public void run() {
                exception.onError(e);
            }
        });
    }
}
