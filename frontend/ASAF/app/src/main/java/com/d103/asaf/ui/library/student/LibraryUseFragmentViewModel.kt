package com.d103.asaf.ui.library.student

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Book
import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "유저도서"
class LibraryUseFragmentViewModel: ViewModel() {
    var curClass = MutableStateFlow(Classinfo(0,0,0,0,0))

    private var _classInfoes = mutableListOf<Classinfo>()
    val classInfoes = _classInfoes

    // <!---------------------------- 전체리스트 ------------------------------->
    private var _books = MutableStateFlow(mutableListOf<Book>())
    val books = _books

    // <!---------------------------- 내가 대출한 리스트 ------------------------------->
    private var _myDraws = MutableStateFlow(mutableListOf<Book>())
    val myDraws = _myDraws

    // 대출기간
    private var _days = MutableStateFlow(listOf(1,2,3,4,5,6,7));
    val days = _days

    var isFirst = true

    init {
        loadFirst()
    }

    // <!---------------------------- 공통 배치 함수 ------------------------------->
    // 관리하는 반 정보를 가장 먼저 가져와야함
    private fun loadFirst() {
        _classInfoes = ApplicationClass.mainClassInfo

        // 첫번째 반을 최초 반으로 설정
        loadCommon()
    }

    fun loadRemote() {
        // 내가 빌린 전체 도서 정보
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.libraryService.getMyDraws(curClass.value.userId)
                }
                if (response.isSuccessful) {
                    _myDraws.value = response.body() ?: mutableListOf<Book>()
                    Log.d(TAG, "loadRemote: 내가대출한도서 ${_myDraws.value}")
                } else {
                    Log.d(TAG, "모든 대출 도서 가져오기 네트워크 오류 $response")
                }
            } catch (e: Exception) {
                Log.d(TAG, " 도서 가져오기 네트워크 오류")
            }
        }

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.libraryService.getBooks(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode)
                }
                if (response.isSuccessful) {
                    _books.value = response.body() ?: mutableListOf<Book>()
                } else {
                    Log.d(TAG, "내 대출 도서 가져오기 네트워크 오류")
                }

                val allResponse = withContext(Dispatchers.IO) {
                    RetrofitUtil.libraryService.getBooks(curClass.value.classCode, curClass.value.regionCode, curClass.value.generationCode)
                }
                if (allResponse.isSuccessful) {
                    _books.value = (allResponse.body()?.map { book ->
                        Book(
                            book.id,
                            book.classNum,
                            book.classCode,
                            book.regionCode,
                            book.generationCode,
                            book.userId,
                            book.bookName,
                            book.author,
                            book.publisher,
                            Long.MAX_VALUE,
                            Long.MAX_VALUE,
                            book.borrowState,
                            book.borrower,
                            book.bookNameCount,
                            book.trueBorrowStateCount
                        )
                    } ?: mutableListOf<Book>()) as MutableList<Book>
                    isFirst = false
                }
            } catch (e: Exception) {
                Log.d(TAG, " 도서 가져오기 네트워크 오류")
            }
        }
    }

    private fun loadCommon() {
        if(ApplicationClass.mainClassInfo.size > 0) curClass.value = ApplicationClass.mainClassInfo[0]
        loadRemote()
    }
}