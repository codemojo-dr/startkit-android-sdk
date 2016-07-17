package io.codemojo.sdk.exceptions;

/**
 * Created by shoaib on 24/06/16.
 */
public class SDKInitializationException extends Exception {

    public SDKInitializationException() {
        super("SDK Not Initialized");
    }
}
