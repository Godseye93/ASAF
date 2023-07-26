package com.d103.asaf.ui.op

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar

// 외부 저장소에서 받아오는 리스트는 MutableStateFlow로 받아온다
class OpFragmentViewModel: ViewModel() {
    // <!---------------------------- 공통 배치 변수 ------------------------------->
    // OpFragment의 textWather에 반 / 월 정보가 바뀌면 리스트 업데이트 하는 코드 삽입해야할 듯
    var curClass = MutableStateFlow(0)
    var curMonth = MutableStateFlow(0)

    // 월 리스트
    private val _months = MutableStateFlow(listOf(0,1,2,3,4,5,6,7,8,9,10,11))
    val months = _months

    // 반 리스트
    private val _classes = MutableStateFlow<List<Int>>(listOf(2, 3, 4))
    val classes = _classes

    // <!---------------------------- 자리 배치 변수 ------------------------------->

    // 진짜 자리정보 get으로 가져옴
    // 5x5보다 적을 수 있음
    private var _seat = mutableListOf(1,22,3,4,15,6,17,8)
    val seat = _seat

    // 고정 값 5x5 (이미지뷰 25개)
    private val _position = MutableStateFlow((0..24).toMutableList())
    val position = _position

    // <!---------------------------- 사물함 배치 함수 ------------------------------->
    // 고정 크기 4x20
    private val _lockers = MutableStateFlow(mutableListOf<Int>())
    val lockers = _lockers

    // <!---------------------------- 서명 배치 함수 ------------------------------->
    private val _moneys = MutableStateFlow(mutableListOf<String>("https://play-lh.googleusercontent.com/Ob9Ys8yKMeyKzZvl3cB9JNSTui1lJwjSKD60IVYnlvU2DsahysGENJE-txiRIW9_72Vd"))
    val moneys = _moneys

    init{
        loadCommon()
        loadSeats()
        loadLockers()
        initCollect()
    }

    // <!---------------------------- 공통 배치 함수 ------------------------------->
    private fun loadCommon() {
        curClass.value = _classes.value[0]
        curMonth.value = _months.value[0]
    }

    private fun initCollect() {
        CoroutineScope(Dispatchers.IO).launch {
            curClass.collect { newClass ->
                // GET해서 가져온 정보 업데이트
                // _seat = GETBY(NEWCLASS)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            curMonth.collect { newMonth ->
                // GET해서 가져온 정보 업데이트
                // _moneys = GETBY(NEWMONTH)
            }
        }
    }

    // <!---------------------------- 자리 배치 함수 ------------------------------->
    // 외부에서 가져온 리스트 값을 5x5 이미지뷰에 차례로 넣어준다
    private fun loadSeats() {
        val fin = _seat.size
        val remainingNumbers = _position.value.filterNot { it in _seat }
        // 차례대로 불러온 자리 채워넣기
        for(i in 0 until fin) _position.value[i] = _seat[i]
        // 나머지 자리 (0~24중) 들어가지 않은 숫자를 이미지 뷰에 넣기
        for(i in fin until 25) _position.value[i] = remainingNumbers[i-fin]
    }

    // <!---------------------------- 사물함 배치 함수 ------------------------------->
    private fun loadLockers() {
        for(i in 0 until 4*20) _lockers.value.add(i)
    }

    fun setSeat(newSeat: MutableList<Int>) {
        _seat = newSeat
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