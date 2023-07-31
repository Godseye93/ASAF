package com.ssafy.template.util

import android.content.Context
import android.content.SharedPreferences
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Member


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

    fun addUserByEmailAndPwd(member : Member){
        val editor = preferences.edit()
        editor.putInt("id", member.id)
        editor.putString("memberName", member.memberName)
        editor.putString("memberEmail", member.memberEmail)
        editor.putString("memberPassword", member.memberPassword)
        editor.putString("authority", member.authority)
        editor.apply()
    }

    fun addFCMToken(token : String){
        val editor = preferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun deleteUser() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }


}