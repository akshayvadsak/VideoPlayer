package com.jv.mxvideoplayer.mxv.videoplayer.CustomClass;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jigs patel on 20-1-2020.
 */

public class SharedPref {

    public static final String MyPref = "MyPref";

    public static final String playerBrightness = "playerBrightness";
    public static final String sortType = "sortType";
    public static final String lastPlayed = "lastPlayed";
    public static final String textTypeFace = "textTypeFace";

    public static final String vibrate = "vibrate";
    public static final String oneTime = "oneTime";
    public static final String securityQue = "securityQue";
    public static final String securityAns = "securityAns";
    public static final String pinLock = "pinLock";

    public static String getSharedPrefData(Context context, String key_id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key_id, "0");
    }

    public static String getSharedPrefData(Context context, String key_id, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key_id, defaultValue);
    }

    public static void setSharedPrefData(Context context, String key_id, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key_id, value);
        editor.commit();
    }
}
