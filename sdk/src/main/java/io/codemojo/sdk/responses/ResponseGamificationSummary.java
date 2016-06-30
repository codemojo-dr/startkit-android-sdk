package io.codemojo.sdk.responses;

import io.codemojo.sdk.models.GamificationSummary;
import io.codemojo.sdk.models.GenericResponse;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseGamificationSummary extends GenericResponse {

    private GamificationSummary results;

    public GamificationSummary getSummary() {
        return results;
    }
}
