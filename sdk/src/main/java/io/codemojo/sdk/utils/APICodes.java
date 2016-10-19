package io.codemojo.sdk.utils;

/**
 * Created by shoaib on 24/06/16.
 */
public class APICodes {

    public static final int RESPONSE_SUCCESS = 200;
    public static final int RESPONSE_FAILURE = 400;

    public static final int ACCESS_DENIED = 401;

    public static final int SERVER_EXCEPTION = 500;
    public static final int SERVER_BAD_GATEWAY = 502;
    public static final int SERVER_QUERY_EXCEPTION = 406;

    public static final int SERVICE_NOT_SETUP = 424;
    public static final int SERVICE_REMOTE_EXCEPTION = 444;
    public static final int SERVICE_QUOTA_EXCEEDED = 509;

    public static final int RESOURCE_NOT_FOUND = 404;
    public static final int INVALID_MISSING_FIELDS = 412;

    public static final int REFERRAL_USED_EARNED = 410;
    public static final int WALLET_BALANCE_EXHAUSTED = 411;
    public static final int DUPLICATE_ACTION = 416;
    public static final int OVERFLOW = 413;

}