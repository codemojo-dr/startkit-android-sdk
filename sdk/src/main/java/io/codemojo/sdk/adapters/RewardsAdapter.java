package io.codemojo.sdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.codemojo.sdk.R;
import io.codemojo.sdk.models.BrandReward;
import io.codemojo.sdk.utils.ImageLoader;

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

    static class ViewHolder {
        ImageView icon;
        TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = null;
        final BrandReward p = getItem(position);

        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        v = vi.inflate(R.layout.available_rewards_item, null);

        if (p != null) {
            ViewHolder holder = (ViewHolder) v.getTag();

            ((TextView) v.findViewById(R.id.rewardTitle)).setText(p.getOffer());

            ImageView image = (ImageView) v.findViewById(R.id.imageBrandLogo);

            ImageLoader imgLoader = new ImageLoader(getContext());
            imgLoader.DisplayImage(p.getLogo(), R.drawable.icon, image);
        }

        return v;
    }
}
