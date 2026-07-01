package com.trang.kidsenglishai.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "kids_session";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "child_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_AGE = "age";
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLogin(int userId, String childName, String email, int age) {
        prefs.edit()
                .putInt(KEY_ID, userId)
                .putString(KEY_NAME, childName)
                .putString(KEY_EMAIL, email)
                .putInt(KEY_AGE, age)
                .apply();
    }

    public void saveLogin(String childName, String email, int age) {
        saveLogin(0, childName, email, age);
    }

    public int getUserId() { return prefs.getInt(KEY_ID, 0); }
    public String getChildName() { return prefs.getString(KEY_NAME, "Bé yêu"); }
    public String getUserName() { return getChildName(); }
    public String getEmail() { return prefs.getString(KEY_EMAIL, ""); }
    public int getAge() { return prefs.getInt(KEY_AGE, 0); }
    public boolean isLoggedIn() { return !getEmail().isEmpty(); }
    public void clear() { prefs.edit().clear().apply(); }
    public void logout() { clear(); }
}
