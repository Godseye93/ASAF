package com.d103.asaf.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.launch
import java.lang.Exception

class ScheduleFragmentViewModel : ViewModel() {

     var notiList = mutableListOf<Noti>()

   fun getMeesage(sender : Int, date : Long){
       Log.d("공지 GET", "보내는 사람:$sender, 시간 : $date ")
       try {
           viewModelScope.launch {
               val response = RetrofitUtil.notiService.getTodayMessage(sender, date)
               if(response.isSuccessful){
                   val responseBody = response.body()
                   Log.d("NOTI LIST", "공지 BODY: $responseBody")
                   if(responseBody!!.isEmpty()){
                       Log.d("NOTI LIST", "공지들: $notiList")
                       notiList.clear()
                   }
                   else{
                       notiList  = responseBody
                   }
               }
               else{
                   Log.d("공지 받기 에러", "error: ${response}")
                   notiList = mutableListOf()
               }
           }
       }catch (e : Exception){

           Log.d("공지 받기 에러", "error:${e.message} ")
       }


   }
}