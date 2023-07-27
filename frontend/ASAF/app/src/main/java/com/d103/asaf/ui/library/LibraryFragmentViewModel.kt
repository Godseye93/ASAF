package com.d103.asaf.ui.library

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class LibraryFragmentViewModel: ViewModel() {
    // <!---------------------------- 공통변수 ------------------------------->
    var curClass = MutableStateFlow(0)

    // 반 리스트
    private val _classes = MutableStateFlow<List<Int>>(listOf(2, 3, 4))
    val classes = _classes

    // <!---------------------------- 전체리스트 ------------------------------->
    private val _books = MutableStateFlow(MutableList(25) { "이거어디까지흘러가는거에요오오?" })
    val books = _books

    // <!---------------------------- 대출한 리스트 ------------------------------->
    private val _myBooks = MutableStateFlow(mutableListOf("find this","what the"))
    val myBooks = _myBooks

    private fun loadCommon() {
        curClass.value = _classes.value[0]
    }

}