package io.codemojo.sdk.network;


import io.codemojo.sdk.responses.ResponseWalletBalance;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by shoaib on 22/10/14.
 */
public interface IWallet {

    @GET("/v1/services/wallet/credits/balance/{user}")
    Call<ResponseWalletBalance> getBalance(@Path("user") String customer_id);

}
