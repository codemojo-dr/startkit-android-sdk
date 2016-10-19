package io.codemojo.sdk.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import io.codemojo.sdk.Codemojo;
import io.codemojo.sdk.R;
import io.codemojo.sdk.adapters.InfiniteScrollListener;
import io.codemojo.sdk.adapters.WalletTransactionAdapter;
import io.codemojo.sdk.facades.ResponseAvailable;
import io.codemojo.sdk.models.PaginatedTransaction;
import io.codemojo.sdk.models.WalletTransaction;
import io.codemojo.sdk.services.GamificationService;

public class GamificationTransactions extends AppCompatActivity {

    private ListView listTransactions;
    private WalletTransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamification_transactions);

        final GamificationService gamificationService = new GamificationService(Codemojo.getAuthenticationService(), null);

        listTransactions = (ListView) findViewById(R.id.lstTransactions);


        gamificationService.getGamificationTransactions(10, new ResponseAvailable() {
            @Override
            public void available(Object result) {
                PaginatedTransaction<WalletTransaction> transactions = (PaginatedTransaction<WalletTransaction>) result;
                List<WalletTransaction> walletTransactions = transactions.getData();
                adapter = new WalletTransactionAdapter(GamificationTransactions.this,
                        android.R.layout.simple_list_item_1, walletTransactions);
                listTransactions.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        listTransactions.setOnScrollListener(new InfiniteScrollListener(10) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                gamificationService.nextTransaction(10, new ResponseAvailable() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void available(Object result) {
                        PaginatedTransaction<WalletTransaction> transactions = (PaginatedTransaction<WalletTransaction>) result;
                        adapter.addAll(transactions.getData());
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });

    }

}
