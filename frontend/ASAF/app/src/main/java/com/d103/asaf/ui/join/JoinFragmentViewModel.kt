package com.d103.asaf.ui.join

import android.os.Build.VERSION_CODES.P
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

private const val TAG = "JoinFragmentViewModel_cjw"
class JoinFragmentViewModel : ViewModel() {

    // email 중복 체크 여부를 담는 MutableLiveData
    private val _isEmailDuplicated = MutableLiveData(false)
    val isEmailDuplicated: LiveData<Boolean> get() = _isEmailDuplicated


    // 회원가입 성공 여부를 담는 MutableLiveData
    private val _isJoinSuccessful = MutableLiveData(false)
    val isJoinSuccessful: LiveData<Boolean> get() = _isJoinSuccessful


    fun signup( member: Member ) {
        // 회원가입 로직을 처리하는 메서드입니다.
        // 이 부분에서는 보통 서버와의 통신을 통해 사용자 정보를 등록하는 처리를 수행합니다.
        // 예시로, 사용자 정보를 출력하기만 하도록 했습니다.
        val userInfo = "이름: ${member.memberName}, 이메일: ${member.memberEmail}, 비밀번호: ${member.memberPassword}, " +
                "생년월일: ${member.birthDate}, 추가정보: ${member.memberInfo}"
//        val member = Member(name, email, password, Date(birth)) // 수정된 생성자 호출
        Log.d(TAG, "signup: $userInfo")

        viewModelScope.launch {
            try {
                // RetrofitUtil을 사용하여 서버에 회원가입 요청
                _isJoinSuccessful.value = RetrofitUtil.memberService.insert(member)
            } catch (e: Exception) {
                // 오류 처리 로직
                Log.d(TAG, "signup: 오류 발생")
                Log.d(TAG, "signup: $e")
            }
        }
    }

    fun validateInputs(
        member: Member,
        confirmPassword: String
//        name: String,
//        email: String,
//        password: String,
//        confirmPassword: String,
//        birth: String,
//        information: String
    ): Boolean {
        // 입력 값의 유효성을 검사합니다.
        if (member.memberName.isBlank() || member.memberEmail.isBlank() ||
            member.memberPassword.isBlank() || confirmPassword.isBlank() ||
            member.birthDate.isBlank() || member.memberInfo.isBlank()) {
            // 입력값 중 하나라도 비어있을 경우 false를 반환합니다.
            return false
        }

        // 비밀번호와 확인 비밀번호가 일치하는지 확인합니다.
        if (member.memberPassword != confirmPassword) {
            // 비밀번호와 확인 비밀번호가 일치하지 않을 경우 false를 반환합니다.
            return false
        }

        // 모든 유효성 검사를 통과한 경우 true를 반환합니다.
        return true
    }

}
