package io.codemojo.sdk.responses;

import io.codemojo.sdk.models.GenericResponse;
import io.codemojo.sdk.models.ReferralCode;

/**
 * Created by shoaib on 26/10/14.
 */
public class ResponseReferralCode extends GenericResponse {

    private ReferralCode results;

    public ReferralCode getReferralCode(){
        return  results;
    }
}
