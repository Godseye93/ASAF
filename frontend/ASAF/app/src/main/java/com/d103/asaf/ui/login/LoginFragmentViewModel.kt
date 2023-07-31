package com.d103.asaf.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.api.MemberService
import com.d103.asaf.common.util.RetrofitUtil.Companion.memberService
import com.google.android.datatransport.Transport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.PasswordAuthentication
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.d103.asaf.common.model.dto.Member

private const val TAG = "LoginFragmentViewModel_cjw"
class LoginFragmentViewModel : ViewModel() {

    // 가상의 로그인 결과를 MutableLiveData로 표현 (실제로는 서버와의 통신 등이 필요)
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // 생성한 memberService.login() 함수를 사용하여 로그인 요청을 서버에 전달합니다.
            val body = Member(memberEmail = email, memberPassword = password)
            val response = memberService.login(body)

            // 서버의 응답을 처리하고 _loginResult에 값을 설정합니다.
            if (response != null) {
                _loginResult.postValue(true)
                Log.d(TAG, "login: 로그인 되었습니다.")
            } else {
                _loginResult.postValue(false)
                Log.d(TAG, "login: 아이디 혹은 비밀번호를 확인하세요.")
            }
        }
    }


    // 예시를 위한 임시 가상의 로그인 메서드
    private fun performFakeLogin(email: String, password: String): Boolean {
        // 여기에 실제 로그인 로직을 구현하고 결과를 반환합니다.
        // 이 예시에서는 email이 "example@example.com", password가 "password"일 때만 로그인 성공으로 가정합니다.
        return (email == "example@example.com" && password == "password")
    }

    // 이메일로 비밀번호 찾기 결과를 관찰할 LiveData
    private val _passwordFindResult = MutableLiveData<Boolean>()
    val passwordFindResult: LiveData<Boolean> get() = _passwordFindResult

    // 이메일로 비밀번호 찾기 요청 함수
    fun findPassword(name: String, email: String, birth: String, information: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Retrofit 서비스를 사용하여 서버에 비밀번호 찾기 요청 보내기
            val response = memberService.getUserInfo(email)

            // 서버 응답 처리
            if (response.isSuccessful) {
                val member = response.body()
                // 서버로부터 받아온 회원 정보와 입력한 정보 비교
                if (member?.memberName == name && member.memberEmail == email
                    && member.birthDate == birth && member.memberInfo == information) {
                    // 일치하는 회원 정보가 있으면 이메일로 비밀번호 전송 성공
                    val passwordResetSubject = "비밀번호 재설정 요청"
                    val passwordResetBody = "안녕하세요, 비밀번호 재설정을 위한 링크가 아래에 있습니다.\n\nhttps://example.com/reset_password?email=$email"
//                    sendEmail(email, passwordResetSubject, passwordResetBody)
//                    Toast.makeText(context, "비밀번호를 이메일로 전송했습니다.", Toast.LENGTH_LONG).show()
                    _toastMessage.postValue("비밀번호를 이메일로 전송했습니다.") // Toast 메시지 설정
                    // sendEmail(email, passwordResetSubject, passwordResetBody)
                    _passwordFindResult.postValue(true)
                    Log.d(TAG, "findPassword: 기존에 있던 회원입니다 !!!! ${email} ${name} ${member.memberPassword}")
                } else {
                    // 일치하는 회원 정보가 없음
                    _passwordFindResult.postValue(false)

                }
            } else {
                // 서버 통신 실패
                _passwordFindResult.postValue(false)
            }
        }
    }
}
