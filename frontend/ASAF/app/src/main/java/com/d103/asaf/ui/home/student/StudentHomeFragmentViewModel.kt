package com.d103.asaf.ui.home.student

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d103.asaf.R
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.DocSeat
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "StudentHomeFragmentView ASAF"
class StudentHomeFragmentViewModel : ViewModel(){
    var nthValue = MutableLiveData<Int>()
    var regionValue = MutableLiveData<String>()
    var classCodeValue = MutableLiveData<Int>()
    var curMySeat = MutableLiveData<Int>()
    var id = 0

    suspend fun addClassInfo(email: String) {
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

                    loadMySeat(classCodeValue.value!!, classInfoResponse.body()!![0].regionCode, ApplicationClass.mainClassInfo[0].generationCode, id)
                } else {
                }
            } else {
                // 서버 통신 실패
            }
        } catch (e: Exception) {

        }
    }
    // 개별자리가져오기
    private suspend fun loadMySeat(ccode: Int, rcode: Int, gcode: Int, uid: Int) {
        try {
            // 개별 자리 가져오기
            val response = withContext(Dispatchers.IO) { RetrofitUtil.opService.getSeat(ccode, rcode, gcode, uid)}
            if (response.isSuccessful) {
                curMySeat.postValue(response.body()?.seatNum ?: 0)
                Log.d(TAG, "loadMySeat: ${curMySeat.value}")
            } else {
                // 서버 통신 실패
                Log.d(TAG, "loadMySeat: 개별 자리 네트워크 에러 $response")
                curMySeat.postValue(0)
            }
        } catch (e: Exception) {
            Log.d(TAG, "loadMySeat: 개별 자리 에러")
        }
    }
}