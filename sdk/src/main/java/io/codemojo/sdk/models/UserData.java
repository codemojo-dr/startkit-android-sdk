package io.codemojo.sdk.models;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shoaib on 11/08/16.
 */
public class UserData implements Serializable {

    private Map<String, Object> map;

    private String name;
    private String email;
    private String mobile;
    private String appleId;
    private String windowsId;
    private String googleId;
    private String timeZone;

    public UserData() {
        map = new HashMap<>();
    }

    public String getName() {
        return (String) map.get("name");
    }

    public void setName(String name) {
        map.put("name", name);
    }

    public String getEmail() {
        return (String) map.get("email");
    }

    public void setEmail(String email) {
        map.put("email", email);
    }

    public String getMobile() {
        return (String) map.get("mobile");
    }

    public void setMobile(String mobile) {
        map.put("mobile", mobile);
    }

    public String getAppleId() {
        return (String) map.get("apn");
    }

    public void setAppleId(String appleId) {
        map.put("apn", appleId);
    }

    public String getWindowsId() {
        return (String) map.get("wpn");
    }

    public void setWindowsId(String windowsId) {
        map.put("wpn", windowsId);
    }

    public String getGoogleId() {
        return (String) map.get("gcm");
    }

    public void setGoogleId(String googleId) {
        map.put("gcm", googleId);
    }

    public String getTimeZone() {
        return (String) map.get("tz");
    }

    public void setTimeZone(String timeZone) {
        map.put("tz", timeZone);
    }

    public String toJson(){
        return toString();
    }

    @Override
    public String toString() {
        Gson json = new Gson();
        return json.toJson(map);
    }
}
