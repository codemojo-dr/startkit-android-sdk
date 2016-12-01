package io.codemojo.sdk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.codemojo.sdk.R;
import io.codemojo.sdk.models.BrandReward;
import io.codemojo.sdk.models.Milestone;

/**
 * Created by shoaib on 15/07/16.
 */
public class MileStonesAdapter extends ArrayAdapter<Milestone> {

    public MileStonesAdapter(Context context, int resource, List<Milestone> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        final Milestone p = getItem(position);

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.available_rewards_item, null);
        }


        if (p != null) {
            TextView description = (TextView) v.findViewById(R.id.rewardTitle);
            description.setText(p.getMilestoneText());

            ImageView img = (ImageView) v.findViewById(R.id.imageBrandLogo);
            if(p.getImage() > 0) {
                Picasso.with(getContext()).load(p.getImage()).placeholder(R.drawable.icon).into(img);
            } else {
                img.setVisibility(View.GONE);
            }
        }

        return v;
    }
}
