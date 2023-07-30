package com.d103.asaf.ui.library.student

import androidx.lifecycle.ViewModel
import com.d103.asaf.common.model.dto.Book
import kotlinx.coroutines.flow.MutableStateFlow

class LibraryUseFragmentViewModel: ViewModel() {
    // <!---------------------------- 전체리스트 ------------------------------->
    private var _books = MutableStateFlow(MutableList(25) { Book(bookName = "이거 어디까지 올라가는 거에요?") })
    val books = _books

    // <!---------------------------- 내가 대출한 리스트 ------------------------------->
    private var _myDraws = MutableStateFlow(mutableListOf(Book(bookName = "find this"), Book(bookName = "what this")))
    val myDraws = _myDraws

    // <!-----------------------------반납 하려는 도서 정보-------------------------------->
    var bookDraw = Book()
}