package com.nullpointertech.cLANConnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * PreferenceUtils.java
 * Helper class to get the preferences stored in the phone
 * more easily.
 */
public class PreferenceUtils {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PreferenceUtils(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setPrefrences(String key, String value) {
        editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPrefrences(String key, int value) {
        editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void setPrefrences(String key, boolean value) {
        editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getPrefrence(String key, String Default) {
        return pref.getString(key, Default);
    }

    public int getPrefrence(String key, int Default) {
        return pref.getInt(key, Default);
    }

    public boolean getPrefrence(String key, boolean Default) {
        return pref.getBoolean(key, Default);
    }

    public void deletePref(String[] key) {
        editor = pref.edit();
        for (int i = 0; i < key.length; i++)
            editor.remove(key[i]);
        editor.apply();
    }

    public void deletePref(String key) {
        editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }
}