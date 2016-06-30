package io.codemojo.sdk.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shoaib on 24/06/16.
 */
public class NoCacheInterceptor implements Interceptor {

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("Cache-Contro","no-cache, no-store, must-revalidate")
                .addHeader("Pragma","no-cache").build();
        return chain.proceed(request).newBuilder().code(200).build();
    }

}