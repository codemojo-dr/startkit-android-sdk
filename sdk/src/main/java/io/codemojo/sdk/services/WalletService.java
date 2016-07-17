package io.codemojo.sdk.services;

import java.io.IOException;

import io.codemojo.sdk.exceptions.InvalidArgumentsException;
import io.codemojo.sdk.exceptions.SDKInitializationException;
import io.codemojo.sdk.exceptions.SetupIncompleteException;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.WalletTransaction;
import io.codemojo.sdk.network.IWallet;
import io.codemojo.sdk.responses.ResponseWalletBalance;
import io.codemojo.sdk.responses.ResponseWalletTransaction;
import retrofit2.Call;

/**
 * Created by shoaib on 16/06/16.
 */
public class WalletService extends BaseService {

    private final IWallet walletService;
    private int transactionPage = 0;

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
        if (walletService == null){
            raiseException(new SDKInitializationException());
            return;
        }
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
                                break;
                            case 400:
                                raiseException(new SetupIncompleteException(code.getMessage()));
                                break;
                            case 200:
                                 moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(code.getBalance());
                                    }
                                });
                                break;
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
    }

    public WalletService getWalletTransactions(int count, final ResponseAvailable callback){
        return nextTransaction(count, callback);
    }

    private WalletService getWalletTransactions(int count, final ResponseAvailable callback, int page){
        if (walletService == null){
            raiseException(new SDKInitializationException());
            return null;
        }
        final Call<ResponseWalletTransaction> response = walletService.getTransaction(getCustomerId(), count, -1, page);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ResponseWalletTransaction code  = response.execute().body();
                    if(code != null){
                        switch (code.getCode()){
                            case -403:
                                raiseException(new InvalidArgumentsException(code.getMessage()));
                                break;
                            case 400:
                                raiseException(new SetupIncompleteException(code.getMessage()));
                                break;
                            case 200:
                                moveTo(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.available(code.getTransactions());
                                    }
                                });
                                break;
                        }
                    }
                } catch (IOException ignored) {
                }
            }
        }).start();
        return this;
    }

    public void reset(){
        transactionPage = 1;
    }

    public WalletService prevTransaction(int count, ResponseAvailable callback){
        transactionPage--;
        if(transactionPage <= 0){
            transactionPage = 1;
        }
        return getWalletTransactions(count, callback, transactionPage);
    }

    public WalletService nextTransaction(int count, ResponseAvailable callback){
        transactionPage++;
        return getWalletTransactions(count, callback, transactionPage);
    }
}
