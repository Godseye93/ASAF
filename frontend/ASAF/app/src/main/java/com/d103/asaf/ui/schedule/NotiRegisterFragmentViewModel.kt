package com.d103.asaf.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.model.dto.Noti
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.launch

class NotiRegisterFragmentViewModel : ViewModel() {

    val notiList = mutableListOf<Noti>()

    var studentList = mutableListOf<Member>()

    fun getStudents(classInfo : Classinfo, noti : Noti, check : Boolean)  {
        viewModelScope.launch{
            val response = RetrofitUtil.attendenceService.getStudentsInfo(classInfo.classCode, classInfo.regionCode, classInfo.generationCode)
            if(response.isSuccessful){
                val responseBody = response.body()
                Log.d("???????", "getStudents: ${responseBody}")
                studentList = responseBody!!
                for(student in studentList){
                    val newNoti = noti.copy()
                    newNoti.receiver = student.id
                    newNoti.notification =check
                    putNotitoList(newNoti)
                }

            }

        }

    }


    fun putNotitoList(data : Noti){
        Log.d("공지 리스트", "notiList: ${notiList.size}")
        notiList.add(data)
    }

    fun pushNoti(){
        Log.d("공지 보내기", "공지 보내기: $notiList ")
        viewModelScope.launch{
            RetrofitUtil.notiService.pushMessageReservation(notiList)
        }


    }
}