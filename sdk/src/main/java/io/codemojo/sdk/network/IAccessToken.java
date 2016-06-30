package io.codemojo.sdk.network;


import io.codemojo.sdk.models.OAuth;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shoaib on 22/10/14.
 */
public interface IAccessToken {

    @POST("/oauth/app")
    @FormUrlEncoded
    Call<OAuth> getAppSecret(@Field("app_token") String app_token);

}
