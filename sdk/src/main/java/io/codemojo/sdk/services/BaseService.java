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
        setContext(authenticationService.getContext());
        this.customer_id = authenticationService.getCustomerId();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new AuthenticationInterceptor(authenticationService.getAccessToken()))
                .build();

        service = new Retrofit.Builder().baseUrl(Constants.getEndpoint(authenticationService.getEnvironment()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(serviceClass);
    }


    protected String getCustomerId() {
        return customer_id;
    }

    protected Object getService() {
        return service;
    }

    protected void raiseException(Exception exception){
        if(this.exception == null){
            return;
        }
        this.exception.onError(exception);
    }
}
