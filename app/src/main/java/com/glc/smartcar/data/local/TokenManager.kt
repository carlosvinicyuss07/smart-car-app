package com.glc.smartcar.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("smartcar_prefs", Context.MODE_PRIVATE)

    fun salvarToken(token: String) {
        sharedPreferences.edit { putString("JWT_TOKEN", token) }
    }

    fun obterToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    fun limparToken() {
        sharedPreferences.edit { remove("JWT_TOKEN") }
    }
}