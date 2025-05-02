package com.st10083866.prog7313_poe_personalbudgetapp

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {


    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserId(userId: Long) {
        val editor = prefs.edit()
            editor.putLong("USER_ID", userId)
            editor.apply()
    }

    fun getUserId(): Int {
        return prefs.getInt("USER_ID", -1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != -1
    }
}