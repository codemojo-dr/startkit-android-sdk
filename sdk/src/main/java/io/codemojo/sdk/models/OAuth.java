package io.codemojo.sdk.models;

/**
 * Created by shoaib on 26/10/14.
 */
public class OAuth {
    private String access_token;
    private String token_type;
    private String secret;
    private int expires_in;

    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public int getExpiresIn() {
        return expires_in;
    }

    public String getSecret() {
        return secret;
    }
}
