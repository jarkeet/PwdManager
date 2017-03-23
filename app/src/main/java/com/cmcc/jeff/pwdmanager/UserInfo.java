package com.cmcc.jeff.pwdmanager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jeff on 2017/3/21.
 */

public class UserInfo {

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String KEY = "KEY";

    private String tag;
    private String userName;
    private String password;

    public UserInfo() {
    }

    public UserInfo(String tag, String userName, String password) {
        this.tag = tag;
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserInfo(String key, JSONObject jsonObject) {
        this.tag = key;
        this.userName = jsonObject.optString(USERNAME);
        this.password = jsonObject.optString(PASSWORD);
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(USERNAME, userName);
            jsonObject.put(PASSWORD, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "tag : " + tag + "-->"
                + "value : " + jsonObject.toString();
    }
}
