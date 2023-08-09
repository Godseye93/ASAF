package com.ssafy.template.util

import android.content.Context
import android.content.SharedPreferences
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.ui.sign.Point
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


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
        editor.putInt("student_number", member.studentNumber)
        editor.putString("memberName", member.memberName)
        editor.putString("memberEmail", member.memberEmail)
        editor.putString("memberPassword", member.memberPassword)
        editor.putString("memberInfo", member.memberInfo)
        editor.putString("birth_date", member.birthDate)
        editor.putInt("electronic_student_id", member.electronicStudentId)
        editor.putString("phone_number", member.phoneNumber)
        editor.putString("profile_image", member.profileImage)
        editor.putInt("team_num", member.teamNum)
        editor.putString("token", member.token)
        editor.putString("attended", member.attended)
//        editor.putString("entryTime", member.entryTime)
//        editor.putString("exitTime", member.exitTime)
        editor.putString("authority", member.authority)
        editor.apply()
    }

    fun autoLoginIsChecked(isChecked : Boolean) {
        val editor = preferences.edit()
        editor.putBoolean("autoLogin", isChecked)
        editor.apply()
    }

    fun changeProfileImage(url : String){
        val editor = preferences.edit()
        editor.putString("profile_image", url)
        editor.apply()
    }

    fun getBoolean(key : String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun addUserInfo(Nth : Int, region : String, classCode : Int) {
        val editor = preferences.edit()
        editor.putInt("Nth", Nth)
        editor.putString("region", region)
        editor.putInt("classCode", classCode)

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

    fun getInt(key:String): Int {
        return preferences.getInt(key, 0)
    }

    // 서명 사인 저장
    fun savePoints(points: List<Point>) {
        val editor = preferences.edit()

        // List<Point>를 Gson을 사용하여 JSON 형태의 문자열로 변환하여 저장
        val gson = Gson()
        val json = gson.toJson(points)
        editor.putString("signs", json)

        editor.apply()
    }

    fun loadPoints(): List<Point> {
        // 저장된 JSON 문자열을 불러옴
        val json = preferences.getString("signs", null)

        if (json != null) {
            // Gson을 사용하여 JSON 문자열을 List<Point>로 변환하여 반환
            val gson = Gson()
            val type = object : TypeToken<List<Point>>() {}.type
            return gson.fromJson(json, type)
        }

        // 저장된 데이터가 없을 경우 빈 List<Point> 반환
        return emptyList()
    }

//    fun tempEmail(email: String) {
//        val editor = preferences.edit()
//
//        editor.apply()
//    }

}