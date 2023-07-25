package com.d103.asaf.ui.op

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class OpFragmentViewModel: ViewModel() {
    // <!---------------------------- 자리 배치 변수 ------------------------------->
    // 월 리스트
    private val _months = MutableStateFlow(listOf(0,1,2,3,4,5,6,7,8,9,10,11))
    val months = _months

    // 반 리스트
    private val _classes = MutableStateFlow<List<Int>>(listOf(2, 3, 4))
    val classes = _classes

    // 고정 값 5x5
    private val _position = MutableStateFlow(mutableListOf<Int>())
    val position = _position

    // <!---------------------------- 사물함 배치 함수 ------------------------------->
    // 고정 크기 4x20
    private val _lockers = MutableStateFlow(mutableListOf<Int>())
    val lockers = _lockers

    init{
        loadSeats()
        loadLockers()
    }

    // <!---------------------------- 자리 배치 함수 ------------------------------->
    private fun loadSeats() {
        for(i in 0 until 5*5) _position.value.add(i)
    }

    // <!---------------------------- 사물함 배치 함수 ------------------------------->
    private fun loadLockers() {
        for(i in 0 until 4*20) _lockers.value.add(i)
    }


//    private val _seat : MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
//    val seat = _seat
//
//    init {
//        loadSeats()
//    }
//
//    private fun loadSeats() {
//        viewModelScope.launch {
//            ApplicationClass.fireStore.getSeat().collect() {
//                _seat.emit(it)
//            }
//        }
//    }
}