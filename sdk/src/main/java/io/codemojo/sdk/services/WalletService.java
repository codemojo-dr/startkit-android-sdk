package io.codemojo.sdk.services;

import java.io.IOException;

import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.network.IWallet;
import io.codemojo.sdk.responses.ResponseWalletBalance;
import retrofit2.Call;

/**
 * Created by shoaib on 16/06/16.
 */
public class WalletService extends BaseService {

    private final IWallet walletService;

    /**
     * @param authenticationService
     */
    public WalletService(AuthenticationService authenticationService) {
        super(authenticationService, IWallet.class);
        walletService = (IWallet) getService();
    }

    /**
     * @param callback
     */
    public void getWalletBalance(final ResponseAvailable callback) {
        final Call<ResponseWalletBalance> response = walletService.getBalance(getCustomerId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseWalletBalance code  = response.execute().body();
                    if(code != null){
                        switch (code.getCode()){
                            case -403:
                                raiseException(new InvalidArgumentsException(code.getMessage()));
                            case 400:
                                raiseException(new SetupIncompleteException(code.getMessage()));
                            case 200:
                                 moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(code.getBalance());
                                    }
                                });
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
    }

}
