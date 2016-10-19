package io.codemojo.sdk.network;


import io.codemojo.sdk.responses.ResponseWalletBalance;
import io.codemojo.sdk.responses.ResponseWalletTransaction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shoaib on 22/10/14.
 */
public interface IWallet {

    @GET("/v1/services/wallet/credits/balance/{user}")
    Call<ResponseWalletBalance> getBalance(@Path("user") String customer_id);

    @GET("/v1/services/wallet/transactions/{user}/{count}")
    Call<ResponseWalletTransaction> getTransaction(@Path("user") String customer_id, @Path("count") int count,
                                                   @Query("transaction_type") int transaction_type, @Query("page") int page);

}
