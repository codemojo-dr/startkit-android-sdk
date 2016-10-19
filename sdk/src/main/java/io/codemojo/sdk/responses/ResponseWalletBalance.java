package io.codemojo.sdk.responses;

import io.codemojo.sdk.models.GenericResponse;
import io.codemojo.sdk.models.WalletBalance;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseWalletBalance extends GenericResponse {

    private WalletBalance results;

    public WalletBalance getBalance() {
        return results;
    }
}
