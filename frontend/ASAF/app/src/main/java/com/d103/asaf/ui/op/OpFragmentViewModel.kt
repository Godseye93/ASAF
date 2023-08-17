package com.d103.asaf.ui.op

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.DocLocker
import com.d103.asaf.common.model.dto.DocSeat
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.net.SocketTimeoutException

private const val TAG = "운영뷰모델"
// 외부 저장소에서 받아오는 리스트는 MutableStateFlow로 받아온다
class OpFragmentViewModel(): ViewModel() {
    // <!-- flow 최초 collect 등록 시 한번만 실행되도록 하는 뮤텍스 -->
    private var _remoteDataLoaded = false

    // <!---------------------------- 공통 배치 변수 ------------------------------->
    // OpFragment의 textWather에 반 / 월 정보가 바뀌면 리스트 업데이트 하는 코드 삽입해야할 듯
    var curClass = MutableStateFlow(Classinfo(0,0,0,0,0))
    var curMonth = MutableStateFlow(0)

    // 월 리스트
    private val _months = MutableStateFlow(listOf(1,2,3,4,5,6,7,8,9,10,11,12))
    val months = _months

    // 진짜 반 리스트
    private var _classInfoes = mutableListOf<Classinfo>()
    val classInfoes = _classInfoes

//    // 반 id 리스트
////    private val _classes = MutableStateFlow<List<Int>>(listOf(2, 3, 4))
//    private val _classes = MutableStateFlow(mutableListOf<Int>())
//    val classes = _classes

    // 반 리스트
    private val _classSurfaces = MutableStateFlow(mutableListOf<Int>())
    val classSurfaces = _classSurfaces

    // <!---------------------------- 반 멤버 변수 ------------------------------->
    private var _students = MutableStateFlow(mutableListOf<Member>())
    val students = _students

    // <!---------------------------- 자리 배치 변수 ------------------------------->
    // 진짜 자리정보 get
    private var _docSeat = MutableStateFlow(mutableListOf<DocSeat>())
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
    private var _docLockers = MutableStateFlow(mutableListOf<DocLocker>())
    val docLockers = _docLockers
    // 고정 크기 4x20
    private var _lockers = MutableStateFlow(mutableListOf<Int>())
    val lockers = _lockers

    // 실제 학생 수 (사용되는  사물함 수)
    var realLockerNum = 0

    var signProgress = MutableStateFlow(0f)

    // <!---------------------------- 서명 함수 ------------------------------->
//    val testSrc = "https://play-lh.googleusercontent.com/Ob9Ys8yKMeyKzZvl3cB9JNSTui1lJwjSKD60IVYnlvU2DsahysGENJE-txiRIW9_72Vd"
//    private val _moneys = MutableStateFlow(MutableList(25) { testSrc })
    private val _signs = MutableStateFlow(mutableListOf<DocSign>())
    val signs = _signs

    //  서명 이미지 링크만
    private val _signUrls = MutableStateFlow(mutableListOf<String>())
    val signUrls = _signUrls

    var attendedPercent = MutableStateFlow(0f)

    init{
        // 반 정보를 가장먼저 세팅해야함
        loadFirst() // loadCollect, loadRemote, loadCommon 등 초기화 함수 모두 포함
    }

    // <!---------------------------- 공통 배치 함수 ------------------------------->
    // 관리하는 반 정보를 가장 먼저 가져와야함
    private fun loadFirst() {
        // 프로가 관리하는 반 정보
        _classInfoes =  ApplicationClass.mainClassInfo
        // 첫번째 반을 최초 반으로 설정
        loadCommon()
    }
    // areItemsTheSame 에서 false가 나면 뷰가 이동하는 애니메이션이 나오고, areContentsTheSame 에서 false 나면 데이터가 깜빡거리면서 변하는 애니메이션이 나옴!!
    private fun loadRemote() {
//        docLockers = MutableList(80) {index -> DocLocker(name = index.toString(), id = index)}
        viewModelScope.launch {
            try {
                fetchStudentsInfo()
                fetchSeats()
                fetchLockers()
                fetchSigns()
                // 이후 작업은 모두 완료된 후 실행
                // loadSeats()
                // loadLockers()
                loadSignUrls()

                // 작업이 완료되면 lock을 풀어줍니다.
                _remoteDataLoaded = false
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "네트워크 오류 : $e")
            } catch (e: Exception) {
                Log.d(TAG, "네트워크 오류 : $e")
            }
        }
    }

    private suspend fun fetchStudentsInfo() {
        val studentResponse = withContext(Dispatchers.IO) {
            RetrofitUtil.attendenceService.getStudentsInfo(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode)
        }
        if (studentResponse.isSuccessful) {
            _students.value = studentResponse.body() ?: mutableListOf()

            if(_students.value.size != 0){
                signProgress.value = (_signs.value.size.toFloat()/_students.value.size) * 100f
            } else {
                signProgress.value = 0f
            }
        } else {
            Log.d(TAG, "학생 가져오기 네트워크 오류")
        }
    }

    private suspend fun fetchSeats() {
        val seatResponse = withContext(Dispatchers.IO) {
            Log.d(TAG, "현재클래스: ${curClass.value}")
            RetrofitUtil.opService.getSeats(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode)
        }
        if (seatResponse.isSuccessful) {
            val maxSize = if(_students.value.size > 25) 25 else _students.value.size
            _docSeat.value = seatResponse.body() ?: MutableList(maxSize) { index ->
                DocSeat(name = _students.value[index].memberName)
            }
            loadSeats()
        } else {
            Log.d(TAG, " 자리 가져오기 네트워크 오류")
        }
    }

    private suspend fun fetchLockers() {
        val lockerResponse = withContext(Dispatchers.IO) {
            RetrofitUtil.opService.getLockers(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode)
        }
        if (lockerResponse.isSuccessful) {
            // 80개를 임의로 생성, 학생 수 만큼 삽입
            _docLockers.value = MutableList(80) { index -> DocLocker(name = "X", lockerNum = index) }
            val realDocLockers = lockerResponse.body() ?: MutableList(80) { index -> DocLocker(name = "X", lockerNum = index) }
            realLockerNum = realDocLockers.size
            for(i in 0 until realLockerNum) _docLockers.value[i] = realDocLockers[i]
            // loadLockers()
        } else {
            Log.d(TAG, "사물함 가져오기 네트워크 오류")
        }
    }

    private suspend fun fetchSigns() {
        val signResponse = withContext(Dispatchers.IO) {
            RetrofitUtil.opService.getSigns(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode, addZero(curMonth.value))
        }
        if (signResponse.isSuccessful) {
            _signs.value = signResponse.body() ?: mutableListOf<DocSign>()
            if(_students.value.size != 0){
                signProgress.value = (_signs.value.size.toFloat()/_students.value.size) * 100f
            } else {
                signProgress.value = 0f
            }
            Log.d(TAG, "fetchSigns: ${_signs.value}")
            loadSignUrls()
        } else {
            Log.d(TAG, "사인 가져오기 네트워크 오류 $signResponse")
            Log.d(TAG, "fetchSigns: $curClass")
            _signs.value = mutableListOf<DocSign>()
        }
    }

    private fun loadCommon() {
        // classinfoes 를 classes로 가공
        loadClasses()

        if(_classInfoes.size > 0) curClass.value = _classInfoes[0]
        curMonth.value = _months.value[0]

        // 현재 반설정이 완료되면 collect 리스너를 달아준다.
        initCollect()
    }

    private fun initCollect() {
        CoroutineScope(Dispatchers.IO).launch {
            curClass.collect { newClass ->
                // GET해서 가져온 정보 업데이트 (자리 / 사물함 / 서명)
                Log.d(TAG, "initCollect: 클래스바뀜 ${curClass.value}")
                loadRemoteOnce()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            curMonth.collect { newMonth ->
                // GET해서 가져온 정보 업데이트 (자리 / 사물함 / 서명)
                loadRemoteOnce()
            }
        }
    }

    private fun loadRemoteOnce() {
        if (!_remoteDataLoaded) {
            _remoteDataLoaded = true
            Log.d(TAG, "loadRemoteOnce: 로드리모트 불림")
            loadRemote()
        }
    }

    // <!---------------------------- 자리 배치 함수 ------------------------------->
    // 외부에서 가져온 리스트 값을 5x5 이미지뷰에 차례로 넣어준다
    private fun loadSeats() {
        _seat.value = _docSeat.value.map { it.seatNum }.toMutableList()
        val fin = _seat.value.size
        val remainingNumbers = _position.value.filterNot { it in _seat.value }
        // 차례대로 불러온 자리 채워넣기
        for(i in 0 until fin) _position.value[i] = _seat.value[i]
        // 나머지 자리 (0~24중) 들어가지 않은 숫자를 이미지 뷰에 넣기
        for(i in fin until 25) _position.value[i] = remainingNumbers[i-fin]
    }

    fun callRealSeats() {
        Log.d(TAG, "callRealSeats: 불림")
        viewModelScope.launch {
            try {
                val seatResponse = withContext(Dispatchers.IO) {
                    Log.d(TAG, "현재클래스: ${curClass.value}")
                    RetrofitUtil.opService.getSeats(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode)
                }
                if (seatResponse.isSuccessful) {
                    _docSeat.value = seatResponse.body() ?: MutableList(_students.value.size) { index ->
                        DocSeat(name = _students.value[index].memberName)
                    }
                    Log.d(TAG, "callRealSeats: ${_docSeat.value}")
                } else {
                    Log.d(TAG, " 자리 가져오기 네트워크 오류")
                }
                loadSeats()
            }
            catch (e:Exception) {

            }
        }
    }

    // <!---------------------------- 사물함 배치 함수 ------------------------------->
    private fun loadLockers() {
//        for(i in 0 until 4*20) _lockers.value.add(i)
         _lockers.value =  _docLockers.value.map { it.lockerNum }.toMutableList()
    }

    private fun loadClasses() {
        _classSurfaces.value = _classInfoes.map{it.classCode }.toMutableList()
//        _classes.value = _classInfoes.map{it.classNum }.toMutableList()
    }

    private fun loadSignUrls() {
        _signUrls.value = _signs.value.map{it.imageUrl}.toMutableList()
    }

    // 바뀐 자리 정보를 채워주는 코드
    fun setSeats(position: MutableList<Int>, seatNum: Int): MutableList<DocSeat> {
        Log.d(TAG, "바뀐포지션: $position")
        Log.d(TAG, "바뀐독시트: ${_docSeat.value}")
        val realPos = position.subList(0,seatNum)
        val postSeats = _docSeat.value.subList(0,seatNum)
        for(i in 0 until seatNum) postSeats[i].seatNum = realPos[i]
        return postSeats
    }

    // 바뀐 사물함 정보를 채워주는 코드
    fun setLockerPositions(tempDocLockers: MutableList<DocLocker>): MutableList<DocLocker> {
        for(i in 0 until realLockerNum) tempDocLockers[i].lockerNum = i
        return tempDocLockers
    }

    //
    fun setDocLocker(temp: MutableList<DocLocker>) {
        _docLockers.value = setLockerPositions(temp)
    }

    private fun addZero(curM: Int): String {
        if(curM < 10) return "0$curM"
        else return "$curM"
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