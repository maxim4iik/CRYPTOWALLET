package com.example.cryptowallet.singltons;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserManager {
    private static final String PREF_USER_ID = "user_id";

    private SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserId(String userId) {
        sharedPreferences.edit().putString(PREF_USER_ID, userId).apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(PREF_USER_ID, null);
    }
}