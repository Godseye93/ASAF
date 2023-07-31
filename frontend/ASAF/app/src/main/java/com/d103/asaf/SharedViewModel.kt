package com.d103.asaf

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "SharedViewModel ASAF"
class SharedViewModel : ViewModel() {
     
     
     lateinit var selectedDate : String // 캘린더에서 쓰는 데이터
     
     private val _classInfoList = MutableLiveData<MutableList<Classinfo>>()
     val classInfoList : LiveData<MutableList<Classinfo>>
          get() = _classInfoList

     fun getClassInfo(user : Member){
          viewModelScope.launch {

               try {
                    val response = RetrofitUtil.attendenceService.getClassInfo(user.id)
                    if(response.isSuccessful){
                         val responseBody = response.body()
                         if(!responseBody.isNullOrEmpty()){
                              _classInfoList.postValue(responseBody!!)
                         }
                         else{

                              Log.d(TAG, "통신 ERROR : responseBody가 NULL")
                         }


                    }

               } catch (e : Exception){
                    Log.d(TAG, "통신 에러: ${e.printStackTrace()}")
               }

          }
     }
     
}