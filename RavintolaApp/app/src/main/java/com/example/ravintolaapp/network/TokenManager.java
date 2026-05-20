package com.example.ravintolaapp.network;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "RavintolaPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USER_ID = "user_id";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private static TokenManager instance;

    private TokenManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    public void saveAuthData(String token, String username, String role, int userId) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ROLE, role);
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "Guest");
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, "customer");
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void clearAuthData() {
        editor.clear();
        editor.apply();
    }
}
