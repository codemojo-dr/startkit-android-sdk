package io.codemojo.sdk.utils;

/**
 * Created by shoaib on 24/06/16.
 */
public class Constants {
    public final static String ENDPOINT_SANDBOX = "https://sandbox.codemojo.io";
//    public final static String ENDPOINT_PRODUCTION = "http://lh-drewards-services.192.168.10.42.xip.io:8888";
    public final static String ENDPOINT_PRODUCTION = "https://production.codemojo.io";

    public final static int SANDBOX = 0;
    public final static int PRODUCTION = 1;
    /**
     * @return String
     */
    public static String getEndpoint(int type){
        return type == 1 ? ENDPOINT_PRODUCTION : ENDPOINT_SANDBOX;
    }
}