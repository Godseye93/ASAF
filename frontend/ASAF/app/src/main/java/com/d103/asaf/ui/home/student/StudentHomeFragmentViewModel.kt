package com.d103.asaf.ui.home.student

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "StudentHomeFragmentView ASAF"
class StudentHomeFragmentViewModel : ViewModel(){
    var nthValue = MutableLiveData<Int>()
    var regionValue = MutableLiveData<String>()
    var classCodeValue = MutableLiveData<Int>()
    var curMySeat = MutableLiveData<Int>()

    suspend fun addClassInfo(email: String) {
        var id = 0
        try {
            val response = withContext(Dispatchers.IO) { RetrofitUtil.memberService.getUserInfo(email)}

            if (response.isSuccessful) {
                val member = response.body()
                id = member!!.id
                val classInfoResponse = withContext(Dispatchers.IO) { RetrofitUtil.attendenceService.getClassInfo(id)}
                if (classInfoResponse.isSuccessful) {
                    nthValue.value = when(classInfoResponse.body()!![0].generationCode) {
                        1 -> 9
                        2 -> 10
                        else -> 0
                    }
                    regionValue.value = when(classInfoResponse.body()!![0].regionCode) {
                        1 -> "서울"
                        2 -> "구미"
                        3 -> "대전"
                        4 -> "부울경"
                        5 -> "광주"
                        else -> " - "
                    }
                    classCodeValue.value = classInfoResponse.body()!![0].classCode
                    ApplicationClass.sharedPreferences.addUserInfo(
                        nthValue.value!!,regionValue.value!!, classCodeValue.value!!
                    )
                } else {
                }
            } else {
                // 서버 통신 실패
            }
        } catch (e: Exception) {

        }
    }
    // 개별자리가져오기
//    suspend fun loadMySeat() {
//        try {
//            // 개별 자리 가져오기
//            //val response = withContext(Dispatchers.IO) { RetrofitUtil.opService.getUserInfo(email)}
//            if (response.isSuccessful) {
//                curMySeat = response.body()
//            } else {
//                // 서버 통신 실패
//            }
//        } catch (e: Exception) {
//
//        }
//    }
}