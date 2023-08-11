package com.d103.asaf.ui.noti

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.Room.NotiMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentNotiFragmentViewModel : ViewModel() {

    private val  _notiList = MutableLiveData<List<NotiMessage>>()

    val notiList : LiveData<List<NotiMessage>>
        get() = _notiList


    val allNotiMessages: LiveData<List<NotiMessage>> = liveData(Dispatchers.IO) {
        val data = ApplicationClass.notiMessageDatabase.notiMessageDao.getAll()
        emit(data)
    }

//    fun getAll(){
//        CoroutineScope(Dispatchers.IO).launch {
//            _notiList.value =  ApplicationClass.notiMessageDatabase.notiMessageDao.getAll()
//        }
//
//    }
}