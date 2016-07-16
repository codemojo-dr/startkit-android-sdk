package io.codemojo.sdk.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.codemojo.sdk.R;
import io.codemojo.sdk.models.WalletTransaction;

/**
 * Created by shoaib on 15/07/16.
 */
public class WalletTransactionAdapter extends ArrayAdapter<WalletTransaction> {

    public WalletTransactionAdapter(Context context, int resource) {
        super(context, resource);
    }

    public WalletTransactionAdapter(Context context, int resource, List<WalletTransaction> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.gamification_transaction_item, null);
        }

        WalletTransaction p = getItem(position);

        if (p != null) {
            TextView description = (TextView) v.findViewById(R.id.transactionDescription);
            TextView points = (TextView) v.findViewById(R.id.transactionPoints);

            description.setText(p.getMeta());
            points.setText(String.valueOf((int) p.getTransactionValue()));
            if(p.getTransactionValue() >= 0){
                points.setBackgroundColor(getContext().getResources().getColor(R.color.green));
            } else {
                points.setBackgroundColor(getContext().getResources().getColor(R.color.red));
            }
        }

        return v;
    }
}
