package com.d103.asaf.ui.library.pro

import androidx.lifecycle.ViewModel
import com.d103.asaf.common.model.dto.Book

// 실제로는 BookDTO
class LibraryManagementFragmentViewModel: ViewModel() {
    //도서등록 정보
    var bookInfo: Book = Book()
}