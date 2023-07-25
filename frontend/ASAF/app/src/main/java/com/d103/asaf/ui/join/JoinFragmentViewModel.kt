package com.d103.asaf.ui.join

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.launch
import java.util.Date

private const val TAG = "JoinFragmentViewModel_cjw"
class JoinFragmentViewModel : ViewModel() {

    // 아이디 중복 체크 여부를 담는 MutableLiveData
    private val _isIdChecked = MutableLiveData(false)
    val isIdChecked: LiveData<Boolean> get() = _isIdChecked

    // 회원가입 성공 여부를 담는 MutableLiveData
    private val _isJoinSuccessful = MutableLiveData(false)
    val isJoinSuccessful: LiveData<Boolean> get() = _isJoinSuccessful


    // 아이디 중복 체크 함수
    fun checkIdDuplication(id: String, showToast: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // RetrofitUtil을 사용하여 서버에 아이디 중복 체크 요청
                val isIdDuplicated = RetrofitUtil.memberService.isUsedId(id)
                if(!isIdDuplicated) {
                    _isIdChecked.value = true
                }
                showToast(if (isIdDuplicated) "중복되는 아이디입니다." else "사용 가능한 아이디입니다.")
            } catch (e: Exception) {
                showToast("오류가 발생했습니다.")
            }
        }
    }


    fun signup(
        name: String, email: String, password: String, birth: String, information: String
    ) {
        // 회원가입 로직을 처리하는 메서드입니다.
        // 이 부분에서는 보통 서버와의 통신을 통해 사용자 정보를 등록하는 처리를 수행합니다.
        // 예시로, 사용자 정보를 출력하기만 하도록 했습니다.
        val userInfo = "이름: $name, 이메일: $email, 비밀번호: $password, 생년월일: $birth, 추가정보: $information"
        val member = Member(name, email, password) // 수정된 생성자 호출
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

}
