package io.codemojo.sdk.responses;

import io.codemojo.sdk.models.GamificationStatus;
import io.codemojo.sdk.models.GenericResponse;
import io.codemojo.sdk.models.Loyalty;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseLoyalty extends GenericResponse {

    private Loyalty results;

    public Loyalty getResult() {
        return results;
    }
}
