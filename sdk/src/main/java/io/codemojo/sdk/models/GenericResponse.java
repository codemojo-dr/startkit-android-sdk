package io.codemojo.sdk.models;

/**
 * Created by shoaib on 16/06/16.
 */
public class GenericResponse {

    private int code;
    private String status;
    private String message;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
