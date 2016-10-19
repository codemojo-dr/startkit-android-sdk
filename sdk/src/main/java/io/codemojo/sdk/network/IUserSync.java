package io.codemojo.sdk.network;


import io.codemojo.sdk.models.GenericResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by shoaib on 22/10/14.
 */
public interface IUserSync {

    @FormUrlEncoded
    @POST("/v1/services/data/user")
    Call<GenericResponse> updateUserInfo(@Field("customer_id") String customer_id, @Field("meta") String json_data);


    @FormUrlEncoded
    @POST("/v1/services/data/user/devices")
    Call<GenericResponse> addDevice(@Field("customer_id") String customer_id, @Field("type") int device_type,
                                    @Field("id") String device_id);


    @FormUrlEncoded
    @POST("/v1/services/data/user/tag")
    Call<GenericResponse> addMeta(@Field("customer_id") String customer_id, @Field("name") String name,
                                  @Field("value") Object value);


    @PUT("/v1/services/data/event/{customer_id}/{event_id}")
    Call<GenericResponse> addEvent(@Path("customer_id") String customer_id, @Path("event_id") String event_name);

}
