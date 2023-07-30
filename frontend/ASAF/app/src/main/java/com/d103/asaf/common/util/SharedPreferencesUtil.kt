package com.ssafy.template.util

import android.content.Context
import android.content.SharedPreferences
import com.d103.asaf.common.config.ApplicationClass


class SharedPreferencesUtil(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(ApplicationClass.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun addUserCookie(cookies: HashSet<String>) {
        val editor = preferences.edit()
        editor.putStringSet(ApplicationClass.COOKIES_KEY_NAME, cookies)
        editor.apply()
    }

    fun getUserCookie(): MutableSet<String>? {
        return preferences.getStringSet(ApplicationClass.COOKIES_KEY_NAME, HashSet())
    }

    fun getString(key:String): String? {
        return preferences.getString(key, null)
    }

    fun addUserByEmailAndPwd(email : String, password : String){
        val editor = preferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    fun deleteUser() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }


}