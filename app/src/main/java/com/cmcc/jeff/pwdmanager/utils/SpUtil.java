package com.cmcc.jeff.pwdmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cmcc.jeff.pwdmanager.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jeff on 2017/3/21.
 */

public class SpUtil {

    private static final String FILE_NAME = "data_file";

    public static void save2Sp(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String get4Sp(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void saveUserInfo(Context context, UserInfo user) {
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONObject jsonobj = new JSONObject();
        try {
            jsonobj.put(UserInfo.USERNAME, user.getUserName());
            jsonobj.put(UserInfo.PASSWORD, user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(user.getKey(), RSAUtil.getInstance(context).encryptData(jsonobj.toString()));
        editor.commit();
    }

    public static UserInfo getUserInfo(Context context, String key) {
        UserInfo userInfo = new UserInfo();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String encstring = sp.getString(key, null);
        if(encstring == null) {
            return null;
        }

        try {
            String originStr = RSAUtil.getInstance(context).decodeData(encstring);
            JSONObject jsonObject = new JSONObject(originStr);
            userInfo.setUserInfo(key, jsonObject);
            return  userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
