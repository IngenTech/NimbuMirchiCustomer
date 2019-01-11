package com.iembsys.admin.nimbumirchi.customer.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yogendra Singh on 21-04-2016.
 */
public class SharedPreference {
    private static SharedPreferences sharedPreferences;

    public SharedPreference() {
        super();
    }
    public static void setValue(Context context, String PREF_KEY, String PREF_VALUE) {
        SharedPreferences.Editor editor;
        sharedPreferences =  context.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, Context.MODE_PRIVATE); //1
        editor = sharedPreferences.edit();
        editor.putString(PREF_KEY, PREF_VALUE);
        editor.commit();
    }

    public static void setValue(Context context, String PREF_KEY, boolean PREF_VALUE) {
        SharedPreferences.Editor editor;
        sharedPreferences =  context.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, Context.MODE_PRIVATE); //1
        editor = sharedPreferences.edit();
        editor.putBoolean(PREF_KEY, PREF_VALUE);
        editor.commit();
    }

    public static String getStringValue(Context context, String PREF_KEY) {
        String text;
        sharedPreferences = context.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(PREF_KEY, null);
        return text;
    }

    public static boolean getBooleanValue(Context context, String PREF_KEY) {
        boolean isValue;
        sharedPreferences = context.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, Context.MODE_PRIVATE);
        isValue = sharedPreferences.getBoolean(PREF_KEY, false);
        return isValue;
    }

    public static void clearAllPreferences(Context context){
        SharedPreferences settings = context.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

}
//    public void removeValue(Context context) {
//        SharedPreferences settings;
//        SharedPreferences.Editor editor;
//
//        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        editor = settings.edit();
//
//        editor.remove(PREFS_KEY);
//        editor.commit();
//    }