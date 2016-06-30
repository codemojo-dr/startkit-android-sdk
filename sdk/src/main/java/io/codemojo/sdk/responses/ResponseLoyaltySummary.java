package io.codemojo.sdk.responses;

import io.codemojo.sdk.models.GenericResponse;
import io.codemojo.sdk.models.Loyalty;
import io.codemojo.sdk.models.LoyaltySummary;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseLoyaltySummary extends GenericResponse {

    private LoyaltySummary results;

    public LoyaltySummary getSummary() {
        return results;
    }
}
