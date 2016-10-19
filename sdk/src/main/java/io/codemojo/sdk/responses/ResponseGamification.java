package io.codemojo.sdk.responses;

import io.codemojo.sdk.models.GamificationStatus;
import io.codemojo.sdk.models.GenericResponse;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseGamification extends GenericResponse {

    private GamificationStatus results;

    public GamificationStatus getGamificationStatus() {
        return results;
    }
}
