package com.example.ezemkofi.ui.theme

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String, isLogin: Boolean){
        sharedPreferences.edit().putString("auth_token", token).apply()
        sharedPreferences.edit().putBoolean("is_login", isLogin).apply()
    }

    fun saveFullName(fullName: String){
        sharedPreferences.edit().putString("fullName", fullName).apply()
    }

    fun getToken(): String?{
        return sharedPreferences.getString("auth_token", null)
    }

    fun getFullName(): String?{
        return sharedPreferences.getString("fullName", null)
    }

    fun isLoggedIn(): Boolean{
        return sharedPreferences.getBoolean("is_login", false)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("auth_token").apply()
        sharedPreferences.edit().remove("is_login").apply()
    }
}