package com.d103.asaf.ui.schedule

import androidx.lifecycle.ViewModel
import com.d103.asaf.common.model.dto.Noti

class ScheduleRegisterFragmentViewModel : ViewModel() {

    val notiList = mutableListOf<Noti>()


    fun putNotitoList(data : Noti){
        notiList.add(data)
    }
}