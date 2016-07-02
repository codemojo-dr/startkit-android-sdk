package io.codemojo.sdk.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shoaib on 24/06/16.
 */
public class AuthenticationInterceptor implements Interceptor {

    private String auth_token;

    public AuthenticationInterceptor(String auth_token) {
        this.auth_token = auth_token;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("Authorization","Bearer " + auth_token).build();
        return chain.proceed(request).newBuilder().code(200).build();
    }

}