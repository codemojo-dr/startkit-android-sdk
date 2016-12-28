package io.codemojo.sdk.facades;

import android.content.Intent;

import java.util.List;

import io.codemojo.sdk.models.BrandReward;

/**
 * Created by shoaib on 24/06/16.
 */
public interface RewardsAvailability {

    void processing();

    void available(List<BrandReward> rewards);

    void unavailable();

    void grabbed(Intent data);

    void error(String error);
}
