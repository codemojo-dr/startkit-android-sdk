package io.codemojo.sdk.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import io.codemojo.sdk.R;
import io.codemojo.sdk.models.BrandReward;

/**
 * Created by shoaib on 15/07/16.
 */
public class RewardsAdapter extends ArrayAdapter<BrandReward> {

    public RewardsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public RewardsAdapter(Context context, int resource, List<BrandReward> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        final BrandReward p = getItem(position);

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.available_rewards_item, null);
        }


        if (p != null) {
            TextView description = (TextView) v.findViewById(R.id.rewardTitle);
            description.setText(p.getOffer());

            ImageView img = (ImageView) v.findViewById(R.id.imageBrandLogo);
            Picasso.with(getContext()).load(p.getLogo()).placeholder(R.drawable.icon).into(img);
        }

        return v;
    }
}
