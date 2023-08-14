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
import co.nedim.maildroidx.MaildroidX
import co.nedim.maildroidx.MaildroidXType
import com.d103.asaf.common.config.ApplicationClass
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import com.d103.asaf.common.util.RetrofitUtil.Companion.attendenceService
import com.d103.asaf.ui.sign.SignDrawFragment.Companion.regionCode
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import kotlinx.coroutines.withContext

private const val TAG = "LoginFragmentViewModel_cjw"
class LoginFragmentViewModel : ViewModel() {

    // 가상의 로그인 결과를 MutableLiveData로 표현 (실제로는 서버와의 통신 등이 필요)
    private val _loginResult = MutableLiveData<Member>()
    val loginResult: LiveData<Member> get() = _loginResult

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage


//    fun login(email: String, password: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response1 = memberService.getUserInfo(email)
//            Log.d(TAG, "login: ${response1.body()}")
//            Log.d(TAG, "login: ${response1.body().toString()}")
//            if (response1.isSuccessful) {
//                val member = response1.body()
//                // 서버로부터 받아온 회원 정보와 입력한 정보 비교
//                if (member?.memberPassword == password) {
//                    val body = mapOf("memberEmail" to email, "memberPassword" to password)
//                    val response = memberService.login(response1.body()!!)
////                    val response = memberService.login(email, password)
//                    Log.d(TAG, "login: ${response.toString()}")
//                    if (response == "로그인 성공!") {
//                        _loginResult.postValue(true)
//                        Log.d(TAG, "login: 로그인 되었습니다.")
//                    } else {
//                        _loginResult.postValue(false)
//                        Log.d(TAG, "login: 비밀번호를 확인하세요.")
//                    }
//                }
//            }else{
//                Log.d(TAG, "login: 등록되지 않은 이메일입니다.")
//            }
//            // 생성한 memberService.login() 함수를 사용하여 로그인 요청을 서버에 전달합니다.
////            val body = Member(memberEmail = email, memberPassword = password)
////            val response = memberService.login(member)
//        }
//    }
    fun login(email: String, password: String, token : String) {
        viewModelScope.launch {
            try {
                _loginResult.value = memberService.login(Member(email, password))
                Log.d("TOKENTOKEN", "login: $token")
                Log.d(TAG, "loginID: ${loginResult.value?.id}")
                val response = memberService.addToken(loginResult.value!!.id, token )
                if(response.isSuccessful){
                    Log.d(TAG, "토큰 변환 성공: ")
                }
                else{
                    Log.d(TAG, "토큰 변환 실패: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                _loginResult.value = Member()
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
                    val passwordResetBody = "기존 비밀번호 : \n\n\n ${member.memberPassword}"
//                    sendEmail(email, passwordResetSubject, passwordResetBody)
                    _toastMessage.postValue("비밀번호를 이메일로 전송했습니다.") // Toast 메시지 설정
                    sendEmail("기존 비밀번호 : ${member.memberPassword}")
                    _passwordFindResult.postValue(true)
                    Log.d(TAG, "findPassword: 기존에 있던 회원입니다 !!!! ${email} ${name} ${member.memberPassword}")
                } else {
                    // 일치하는 회원 정보가 없음
                    _passwordFindResult.postValue(false)
                    Log.d(TAG, "findPassword: 정보 일치하지 않음")
                }
            } else {
                // 서버 통신 실패
                _passwordFindResult.postValue(false)
            }
        }
    }

    // 저장 후 메일로 보내 주는 코드 추가
    private fun sendEmail( s : String) {
        MaildroidX.Builder()
            .smtp("live.smtp.mailtrap.io")
            .smtpUsername("api")
            .smtpPassword("0647ceab68282d673bdd53a351635833")
            .port("587")
            .type(MaildroidXType.HTML)
            .to("wpwo98@naver.com")
            .from("mailtrap@asaf.live")
            .subject("hello")
            .body(s)
//            .attachment(path)
            .isStartTLSEnabled(true)
            .mail()

        Log.d("메일", "sendEmail: 보냄")
    }


    suspend fun addClassInfo(email: String) {
        var id = 0
        try {
            val response = withContext(Dispatchers.IO) {memberService.getUserInfo(email)}
            Log.d(TAG, "addClassInfo 1 : ${response.body()}")

            if (response.isSuccessful) {
                val member = response.body()
                id = member!!.id
                Log.d(TAG, "addClassInfo 2 : $id")
                val classInfoResponse = withContext(Dispatchers.IO) {attendenceService.getClassInfo(id)}
                Log.d(TAG, "addClassInfo 리스트에 뭐 담기는지 확인 : $classInfoResponse")
                if (classInfoResponse.isSuccessful) {
                    Log.d(TAG, "addClassInfo getClass: 성공 !")
                    val responseGenerationCode = classInfoResponse.body()!![0].generationCode
                    val responseRegionCode = classInfoResponse.body()!![0].regionCode
                    val responseClassCode = classInfoResponse.body()!![0].classCode
                    ApplicationClass.sharedPreferences.addUserInfo(
                        responseGenerationCode,responseRegionCode.toString(),responseClassCode
                    )
                } else {
                    Log.d(TAG, "addClassInfo getClass: 실패 !")
                    Log.d(TAG, "addClassInfo: ${classInfoResponse.errorBody()}")
                }
            } else {
                // 서버 통신 실패
                Log.d(TAG, "addClassInfo: 서버 통신 실패")
            }
        } catch (e: Exception) {
            Log.d(TAG, "addClassInfo 에러 발생 : $e")
            Log.d(TAG, "addClassInfo: ${e.printStackTrace()}")
        }

    }
}
