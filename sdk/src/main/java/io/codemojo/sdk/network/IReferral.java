package io.codemojo.sdk.network;


import io.codemojo.sdk.models.GenericResponse;
import io.codemojo.sdk.models.ReferralCode;
import io.codemojo.sdk.responses.ResponseReferralCode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by shoaib on 22/10/14.
 */
public interface IReferral {

    @PUT("/v1/services/referral/generate/{user}")
    Call<ResponseReferralCode> getReferralCode(@Path("user") String customer_id);

    @GET("/v1/services/referral/claim/{customer_id}/{code}")
    Call<GenericResponse> applyReferralCode(@Path("customer_id") String customer_id, @Path("code") String code);

    @PUT("/v1/services/referral/claim/{customer_id}")
    Call<GenericResponse> markActionComplete(@Path("customer_id") String customer_id);

}
