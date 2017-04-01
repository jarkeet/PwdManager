package com.cmcc.jeff.pwdmanager;

/**
 * Created by jeff on 2017/3/21.
 */

public class UserInfo {

    public static final String TAG = "tag";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

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

    @Override
    public String toString() {
        return "UserInfo{" +
                "tag='" + tag + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
