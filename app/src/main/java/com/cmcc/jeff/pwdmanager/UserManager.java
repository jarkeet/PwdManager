package com.cmcc.jeff.pwdmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cmcc.jeff.pwdmanager.utils.RSAUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
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
    public static void saveUserInfo(Context context, UserInfo user) throws Exception {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String jsonStr = new Gson().toJson(user);
        editor.putString(user.getTag(), RSAUtil.getInstance(context).encryptData(jsonStr));
        editor.commit();
        addTag(context, user.getTag());
    }

    /**
     * get userInfo by tag
     * @param context
     * @param tag
     * @return
     */
    public static UserInfo getUserInfo(Context context, String tag) throws Exception {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        String encstring = sp.getString(tag, null);
        if(encstring == null) {
            return null;
        }
        String originStr = RSAUtil.getInstance(context).decodeData(encstring);
        UserInfo userInfo = new Gson().fromJson(originStr, UserInfo.class);
        return  userInfo;
    }

    /**
     * delete the user from cache
     * @param user
     */
    public static void removeUserInfo(Context context, UserInfo user) {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String tag = user.getTag();
        List<String> tags = getTags(context);
        if(tags.contains(tag)) {
            tags.remove(tag);
        }
        String jsonStr = new Gson().toJson(tags);
        editor.putString(TAGS_KEY, jsonStr);
        editor.commit();

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
        String jsonStr = new Gson().toJson(tags);
        editor.putString(TAGS_KEY, jsonStr);
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
            return new ArrayList<>();
        }
//        List<String> tagsList= new ArrayList<>(Arrays.asList(tags.split("\\s*,\\s*"))); //matc zero or more whitespace, acomma ,zero or more whitespace
        try {
            List<String> tagsList = new Gson().fromJson(tags, new TypeToken<List<String>>(){}.getType());
            return tagsList;
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }


    }
}
