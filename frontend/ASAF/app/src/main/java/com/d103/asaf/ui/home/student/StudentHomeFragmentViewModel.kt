package com.d103.asaf.ui.home.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentHomeFragmentViewModel : ViewModel(){
    val nthValue = MutableLiveData<Int>()
    val regionValue = MutableLiveData<String>()
    val classCodeValue = MutableLiveData<Int>()
}