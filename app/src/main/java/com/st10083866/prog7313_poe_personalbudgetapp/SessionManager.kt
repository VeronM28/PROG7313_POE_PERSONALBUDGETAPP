package com.st10083866.prog7313_poe_personalbudgetapp

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {


    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserId(userId: String) {
        prefs.edit().putString("USER_ID", userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString("USER_ID", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != "-1"
    }
}
