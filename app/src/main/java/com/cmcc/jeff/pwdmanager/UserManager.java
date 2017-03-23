package com.cmcc.jeff.pwdmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.cmcc.jeff.pwdmanager.utils.RSAUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jeff on 2017/3/21.
 */

public class UserManager {

    private static final String SP_FILE_NAME = "data_file";
    private static final String TAGS_KEY = "users_key";
    private static final String COMMA = " , ";

    public static void save2Sp(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String get4Sp(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * persist save userInfo obj
     * @param context
     * @param user
     */
    public static void saveUserInfo(Context context, UserInfo user) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONObject jsonobj = new JSONObject();
        try {
            jsonobj.put(UserInfo.USERNAME, user.getUserName());
            jsonobj.put(UserInfo.PASSWORD, user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(user.getTag(), RSAUtil.getInstance(context).encryptData(jsonobj.toString()));
        editor.commit();
        addTag(context, user.getTag());
    }

    /**
     * get userInfo by tag
     * @param context
     * @param tag
     * @return
     */
    public static UserInfo getUserInfo(Context context, String tag) {
        UserInfo userInfo = new UserInfo();
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        String encstring = sp.getString(tag, null);
        if(encstring == null) {
            return null;
        }

        try {
            String originStr = RSAUtil.getInstance(context).decodeData(encstring);
            JSONObject jsonObject = new JSONObject(originStr);
            userInfo.setUserInfo(tag, jsonObject);
            return  userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * update and save tags
     * @param context
     * @param tag
     */
    public static void addTag(Context context, String tag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<String> tags = getTags(context);
        tags.add(tag);
        String tagsStr = "";
        for(String s : tags) {
            tagsStr += s + COMMA;
        }
        editor.putString(TAGS_KEY, tagsStr);
        editor.commit();
    }

    /**
     * return tags list
     * @param context
     * @return
     */
    public static List<String> getTags(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        String tags = sharedPreferences.getString(TAGS_KEY, "");
        if(TextUtils.isEmpty(tags)) {
            return new ArrayList<String>();
        }
        List<String> tagsList= new ArrayList<>(Arrays.asList(tags.split("\\s*,\\s*"))); //match zero or more whitespace, acomma ,zero or more whitespace
        return tagsList;

    }
}
