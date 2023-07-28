package com.d103.asaf.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil.Companion.memberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserInfoFragmentViewModel : ViewModel() {

    private val _userInfo = MutableLiveData<Member>()
    val userInfo: LiveData<Member> get() = _userInfo

    fun getUserInfo(memberEmail: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    memberService.getUserInfo(memberEmail)
                }
                if (response.isSuccessful) {
                    _userInfo.value = response.body()
                } else {
                    // Handle error case
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
            }
        }
    }

}