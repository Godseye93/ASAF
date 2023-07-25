package com.d103.asaf.ui.join

import androidx.lifecycle.ViewModel

class JoinFragmentViewModel : ViewModel() {

    fun signup(
        name: String,
        email: String,
        password: String,
        birth: String,
        information: String
    ) {
        // 회원가입 로직을 처리하는 메서드입니다.
        // 이 부분에서는 보통 서버와의 통신을 통해 사용자 정보를 등록하는 처리를 수행합니다.
        // 예시로, 사용자 정보를 출력하기만 하도록 했습니다.
        val userInfo = "이름: $name, 이메일: $email, 비밀번호: $password, 생년월일: $birth, 추가정보: $information"
        println(userInfo)
    }
}
