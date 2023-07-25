package com.d103.asaf.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginFragmentViewModel : ViewModel() {

    // 가상의 로그인 결과를 MutableLiveData로 표현 (실제로는 서버와의 통신 등이 필요)
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    // 실제로는 서버와 통신하여 로그인 처리를 해야하지만, 예시를 위해 가상의 로그인 메서드를 구현합니다.
    fun login(email: String, password: String) {
        // 여기서 실제 로그인 처리 로직을 구현하고 로그인 결과를 _loginResult에 값을 설정합니다.
        // 예시: 성공하면 _loginResult.value = true, 실패하면 _loginResult.value = false
        val success = performFakeLogin(email, password)
        _loginResult.value = success
    }

    // 예시를 위한 임시 가상의 로그인 메서드
    private fun performFakeLogin(email: String, password: String): Boolean {
        // 여기에 실제 로그인 로직을 구현하고 결과를 반환합니다.
        // 이 예시에서는 email이 "example@example.com", password가 "password"일 때만 로그인 성공으로 가정합니다.
        return (email == "example@example.com" && password == "password")
    }
}
