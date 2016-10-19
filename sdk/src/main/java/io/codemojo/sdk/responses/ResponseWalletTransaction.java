package io.codemojo.sdk.responses;

import io.codemojo.sdk.models.GenericResponse;
import io.codemojo.sdk.models.PaginatedTransaction;
import io.codemojo.sdk.models.WalletTransaction;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseWalletTransaction extends GenericResponse {

    private PaginatedTransaction<WalletTransaction> results;

    public PaginatedTransaction<WalletTransaction> getTransactions() {
        return results;
    }
}
