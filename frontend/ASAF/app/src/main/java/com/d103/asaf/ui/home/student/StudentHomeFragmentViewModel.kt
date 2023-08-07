package com.d103.asaf.ui.home.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentHomeFragmentViewModel : ViewModel(){
    private val _nthLiveData = MutableLiveData<Int>()
    val nthLiveData: LiveData<Int>
        get() = _nthLiveData

    private val _regionLiveData = MutableLiveData<Int>()
    val regionLiveData: LiveData<Int>
        get() = _regionLiveData

    private val _classNumLiveData = MutableLiveData<Int>()
    val classNumLiveData: LiveData<Int>
        get() = _classNumLiveData

    // Update nthLiveData
    fun updateNth(nth: Int) {
        _nthLiveData.value = nth
    }

    // Update regionLiveData
    fun updateRegion(region: Int) {
        _regionLiveData.value = region
    }

    // Update classNumLiveData
    fun updateClassNum(classNum: Int) {
        _classNumLiveData.value = classNum
    }
}