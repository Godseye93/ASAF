package com.d103.asaf.ui.op

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OpFragmentViewModel: ViewModel() {
    private val _position = mutableListOf<Int>()
    val position = _position

    private val _locker = mutableListOf<String>()
    val locker = _locker

    init{
        loadSeats()
    }

    private fun loadSeats() {
        for(i in 0 until 5*5) position.add(i)
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