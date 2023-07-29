package com.d103.asaf.ui.op

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.DocLocker
import com.d103.asaf.common.model.dto.DocSeat
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "운영뷰모델"
// 외부 저장소에서 받아오는 리스트는 MutableStateFlow로 받아온다
class OpFragmentViewModel: ViewModel() {
    // <!---------------------------- 공통 배치 변수 ------------------------------->
    // OpFragment의 textWather에 반 / 월 정보가 바뀌면 리스트 업데이트 하는 코드 삽입해야할 듯
    var curClass = MutableStateFlow(0)
    var curMonth = MutableStateFlow(0)

    // 월 리스트
    private val _months = MutableStateFlow(listOf(1,2,3,4,5,6,7,8,9,10,11,12))
    val months = _months

    // 진짜 반 리스트
    private var _classInfoes = mutableListOf<Classinfo>()
    val classInfoes = _classInfoes

    // 반 리스트
//    private val _classes = MutableStateFlow<List<Int>>(listOf(2, 3, 4))
    private val _classes = MutableStateFlow(mutableListOf<Int>())
    val classes = _classes

    // <!---------------------------- 자리 배치 변수 ------------------------------->
    // 진짜 자리정보 get
    private var _docSeat = mutableListOf<DocSeat>()
    val docSeat = _docSeat

    // 5x5보다 적을 수 있음
//    private var _seat = MutableStateFlow(mutableListOf(1,22,3,4,15,6,17,8))
    private var _seat = MutableStateFlow(mutableListOf<Int>())
    val seat = _seat

    // 고정 값 5x5 (이미지뷰 25개)
    private val _position = MutableStateFlow((0..24).toMutableList())
    val position = _position

    // <!---------------------------- 사물함 배치 함수 ------------------------------->
    // 진짜 사물함 정보
    private var _docLockers = mutableListOf<DocLocker>()
    val docLockers = _docLockers

    // 고정 크기 4x20
    private var _lockers = MutableStateFlow(mutableListOf<Int>())
    val lockers = _lockers

    // <!---------------------------- 서명 함수 ------------------------------->
//    val testSrc = "https://play-lh.googleusercontent.com/Ob9Ys8yKMeyKzZvl3cB9JNSTui1lJwjSKD60IVYnlvU2DsahysGENJE-txiRIW9_72Vd"
//    private val _moneys = MutableStateFlow(MutableList(25) { testSrc })
    private val _moneys = MutableStateFlow(mutableListOf<DocSign>())
    val moneys = _moneys

    init{
        // 동기로 가져오기 (withContext)
        initCollect() // loadRemote 포함
        loadCommon()
        // 진짜 정보를 받아서 일부만 사용하는 클래스는 받아온 뒤 mapping 필요
        loadSeats()
        loadClasses()
        loadLockers()
    }

    // <!---------------------------- 공통 배치 함수 ------------------------------->
    private fun loadRemote() {
        // 자리 정보
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.opService.getSeats(curClass.value)
                }
                if (response.isSuccessful) {
                    _docSeat = response.body() ?: mutableListOf<DocSeat>()
                } else {
                    Log.d(TAG, " 자리 가져오기 네트워크 오류")
                }
            } catch (e: Exception) {
                Log.d(TAG, " 자리 가져오기 네트워크 오류")
            }
        }
        // 사물함 정보
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.opService.getLockers(curClass.value)
                }
                if (response.isSuccessful) {
                    _lockers.value = response.body() ?: mutableListOf<DocLocker>()
                } else {
                    Log.d(TAG, "사물함 가져오기 네트워크 오류")
                }
            } catch (e: Exception) {
                Log.d(TAG, " 사물함 가져오기 네트워크 오류")
            }
        }
        // 서명 정보
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.opService.getSigns(curClass.value)
                }
                if (response.isSuccessful) {
                    _moneys.value = response.body() ?: mutableListOf<DocSign>()
                } else {
                    Log.d(TAG, "사인 가져오기 네트워크 오류")
                }
            } catch (e: Exception) {
                Log.d(TAG, "사인 가져오기 네트워크 오류")
            }
        }

        // 프로가 관리하는 반 정보
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    // RetrofitUtil.opService.getClasses(sharedPrefernce 유저 id)
                }
                if (response.isSuccessful) {
                    _classInfoes = response.body() ?: mutableListOf<Classinfo>()
                } else {
                    Log.d(TAG, " 반 정보 가져오기 네트워크 오류")
                }
            } catch (e: Exception) {
                Log.d(TAG, " 반 정보 가져오기 네트워크 오류")
            }
        }
    }

    private fun loadCommon() {
        curClass.value = _classes.value[0]
        curMonth.value = _months.value[0]
    }

    private fun initCollect() {
        CoroutineScope(Dispatchers.IO).launch {
            curClass.collect { newClass ->
                // GET해서 가져온 정보 업데이트 (자리 / 사물함 / 서명)
                // _seat = GETBY(NEWCLASS)
                // _lockers = GETBY(NEWCLASS)
                // _moneys = GETBY(NEWCLASS, NEWMONTH)
                // loadRemote()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            curMonth.collect { newMonth ->
                // GET해서 가져온 정보 업데이트 (자리 / 사물함 / 서명)
                // _seat.value = GETBY(NEWCLASS)
                // _lockers = GETBY(NEWCLASS)
                // _moneys = GETBY(NEWCLASS, NEWMONTH)
                // loadRemote()
            }
        }
    }

    // <!---------------------------- 자리 배치 함수 ------------------------------->
    // 외부에서 가져온 리스트 값을 5x5 이미지뷰에 차례로 넣어준다
    private fun loadSeats() {
        _seat.value = _docSeat.map { it.seatNum }.toMutableList()
        val fin = _seat.value.size
        val remainingNumbers = _position.value.filterNot { it in _seat.value }
        // 차례대로 불러온 자리 채워넣기
        for(i in 0 until fin) _position.value[i] = _seat.value[i]
        // 나머지 자리 (0~24중) 들어가지 않은 숫자를 이미지 뷰에 넣기
        for(i in fin until 25) _position.value[i] = remainingNumbers[i-fin]
    }

    // <!---------------------------- 사물함 배치 함수 ------------------------------->
    private fun loadLockers() {
//        for(i in 0 until 4*20) _lockers.value.add(i)
        _lockers.value =  _docLockers.map { it.lockerNum }.toMutableList()
    }

    private fun loadClasses() {
        _classes.value = _classInfoes.map{it.classNum}.toMutableList()
    }

    // 바뀐 자리 정보로 사물함 정보 교체해주는 코드
    fun setLockers(lockers: MutableList<Int>): MutableList<DocLocker> {
        val loop = lockers.size
        for(i in 0 until loop) _docLockers[i].lockerNum = _lockers.value[i]
        return _docLockers
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