package io.codemojo.sdk.network;


import java.util.Map;

import io.codemojo.sdk.models.ResponseAvailableRewards;
import io.codemojo.sdk.models.ResponseRewardGrab;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by shoaib on 22/10/14.
 */
public interface IRewards {

    @GET("/v1/services/rewards/list/available-rewards/{app_id}")
    Call<ResponseAvailableRewards> getAvailableRewards(@Path("app_id") String app_id, @Query("customer_id") String customer_id);

    @GET("/v1/services/rewards/list/available-rewards/{app_id}")
    Call<ResponseAvailableRewards> getAvailableRewards(@Path("app_id") String app_id, @Query("customer_id") String customer_id,
                                                   @Query("price_min") int priceStartsAt, @Query("price_max") int priceUpto);

    @GET("/v1/services/rewards/list/available-rewards/{app_id}")
    Call<ResponseAvailableRewards> getAvailableRewards(@Path("app_id") String app_id, @Query("customer_id") String customer_id,
                                                   @QueryMap(encoded = true) Map<String, String> options_filters);

    @POST("/v1/services/rewards/grab/{app_id}/{reward_id}")
    @FormUrlEncoded
    Call<ResponseRewardGrab> grabReward(@Path("app_id") String app_id, @Path("reward_id") String reward_id, @Field("customer_id") String customer_id,
                                            @FieldMap(encoded = true) Map<String, String> additional_details);

}
