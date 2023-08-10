package com.d103.asaf.ui.home.pro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.launch

private const val TAG = "ProHomeFragmentViewMode ASAF"
class ProHomeFragmentViewModel : ViewModel() {
    private val _studentInfoList = MutableLiveData<MutableList<Member>>()

    val studentInfoList : LiveData<MutableList<Member>>
        get() = _studentInfoList

    // 해당 반에 해당하는 학생들 정보와 출석 정보 가지고 오기
    fun getStudentsInfo(classCode : Int, regionCode : Int, generationCode : Int) {
        viewModelScope.launch {
            Log.d(TAG, "getStudentsInfo: ")
            try {
                val response = RetrofitUtil.attendenceService.getStudentsInfo(classCode, regionCode, generationCode)
                Log.d(TAG, "get: $response")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d(TAG, "반 정보?????: $responseBody")
                    _studentInfoList.postValue(responseBody!!)
                }
            } catch (e: Exception) {
                Log.d(TAG, "통신 에러: ${e.printStackTrace()}")
//            }
            }
        }
    }

    fun pushMessage(messageList : MutableList<Noti>){
        Log.d(TAG, "보내기")
        Log.d("NOTI", "${messageList} ")
        viewModelScope.launch {

            try{

                val response = RetrofitUtil.notiService.pushMessage(messageList)
                if(true){
                    val responseBody = response.body()
                    Log.d(TAG, "메시지 정상적으로 보냄?: $responseBody")
                }
                else{
                    Log.d(TAG, "pushMessage error: ${response.errorBody()}")

                }

            }catch (e: Exception) {
                Log.d(TAG, "통신 에러: ${e.printStackTrace()}")
//            }
            }
        }
    }

//    private val _classInfoList = MutableLiveData<MutableList<Classinfo>>()

//    val classInfoList : LiveData<MutableList<Classinfo>>
//        get() = _classInfoList


//    fun getClassInfo(user : Member){
//        viewModelScope.launch {
//
//            try {
//                val response = RetrofitUtil.attendenceService.getClassInfo(user.id)
//                if(response.isSuccessful){
//                    val responseBody = response.body()
//                    if(!responseBody.isNullOrEmpty()){
//                        _classInfoList.postValue(responseBody!!)
//                    }
//                    else{
//
//                        Log.d(TAG, "통신 ERROR : responseBody가 NULL")
//                    }
//
//
//                }
//
//            } catch (e : Exception){
//                Log.d(TAG, "통신 에러: ${e.printStackTrace()}")
//            }
//
//        }
//    }


}