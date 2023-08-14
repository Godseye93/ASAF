package com.d103.asaf

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.MarketDetail
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

private const val TAG = "SharedViewModel ASAF"
class SharedViewModel : ViewModel() {

     lateinit var selectedDate : String // 캘린더에서 쓰는 데이터
     var year : Int = 0
     var month : Int = 0
     var day : Int = 0
     var selectedMarketId  = 0 // 마켓 상세 정보 조회할 때 사용
     val _classInfoList = MutableLiveData<MutableList<Classinfo>>()
     lateinit var marketDetail : MarketDetail
     lateinit var userImage : String
     val classInfoList : LiveData<MutableList<Classinfo>>
          get() = _classInfoList

     var logInUser : Member = Member()


     fun InsertMarketDetail(data : MarketDetail){
          marketDetail = data
     }

     fun getClassInfo(user : Member){
          viewModelScope.launch {

               try {
                    Log.d(TAG, "userID: ${user.id}")
                    val response = withContext(Dispatchers.IO){
                         RetrofitUtil.attendenceService.getClassInfo(user.id)
                    }
                    if(response.isSuccessful){
                         val responseBody = response.body()
                         if(!responseBody.isNullOrEmpty()){
                              Log.d(TAG, "반 리스트: $responseBody")
//                              _classInfoList.value = responseBody!!
                              Log.d(TAG, "classInfoList:${_classInfoList.value} ")
                              _classInfoList.postValue(responseBody!!)
                              ApplicationClass.mainClassInfo = responseBody
                              Log.d(TAG, "getClassInfo~~~: ${ApplicationClass.mainClassInfo}")

//                              Log.d(TAG, "getClassInfo~~~: ${responseBody[0].generationCode}")
//                              Log.d(TAG, "getClassInfo~~~: ${responseBody[0].regionCode}")
//                              Log.d(TAG, "getClassInfo~~~: ${responseBody[0].classCode}")
//
                              val nthText = when (responseBody[0].generationCode) {
                                   1 -> 9
                                   2 -> 10
                                   else -> 0
                              }
                              val regionText = when (responseBody[0].regionCode) {
                                   1 -> "서울"
                                   2 -> "구미"
                                   3 -> "대전"
                                   4 -> "부울경"
                                   5 -> "광주"
                                   else -> " - "
                              }
                              val classText = when (responseBody[0].classCode) {
                                   0 -> 0
                                   else -> responseBody[0].classCode
                              }

                              ApplicationClass.sharedPreferences.addUserInfo(
                                   nthText,
                                   regionText,
                                   classText
                              )

                         }
                         else{
                              Log.d(TAG, "통신 ERROR : responseBody가 NULL")
                              Log.d(TAG, "getClassInfo: ${response.errorBody()}")
                         }
                    }
               } catch (e : Exception){
                    Log.d(TAG, "통신 에러: ${e.printStackTrace()}")
               }
          }
     }

     fun postClassInfoList(list : MutableList<Classinfo>){
          _classInfoList.value = list
     }
}