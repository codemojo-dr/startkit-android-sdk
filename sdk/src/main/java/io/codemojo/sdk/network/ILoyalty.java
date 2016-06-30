package io.codemojo.sdk.network;


import io.codemojo.sdk.responses.ResponseLoyalty;
import io.codemojo.sdk.responses.ResponseLoyaltyMaximumRedemption;
import io.codemojo.sdk.responses.ResponseLoyaltySummary;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shoaib on 22/10/14.
 */
public interface ILoyalty {

    @PUT("/v1/services/loyalty")
    @FormUrlEncoded
    Call<ResponseLoyalty> addLoyaltyPoints(@Field("customer_id") String customer_id, @Field("transaction") float transaction,
                                           @Field("transaction_id") String transaction_id, @Field("meta") String meta,
                                           @Field("tag") String tag, @Field("platform") String platform,
                                           @Field("service") String service);

    @GET("/v1/services/loyalty")
    Call<ResponseLoyalty> calculateLoyaltyPoints(@Query("customer_id") String customer_id, @Query("transaction") float transaction_value,
                                                                 @Query("platform") String platform, @Query("service") String service);

    @GET("/v1/services/loyalty")
    Call<ResponseLoyaltyMaximumRedemption> maximumRedemption(@Query("customer_id") String customer_id, @Query("transaction") float transaction_value,
                                                             @Query("platform") String platform, @Query("service") String service);

    @GET("/v1/services/loyalty/summary/%s")
    Call<ResponseLoyaltySummary> summary(@Path("customer_id") String customer_id);


}
