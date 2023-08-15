package com.d103.asaf.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil.Companion.memberService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserInfoFragmentViewMod_cjw_asaf"
class UserInfoFragmentViewModel : ViewModel() {

    private val _userInfo = MutableLiveData<Member>()
    val userInfo: LiveData<Member> get() = _userInfo

    private val _updateResult = MutableLiveData<Member>()
    val updateResult: LiveData<Member> get() = _updateResult

    suspend fun getUserInfo(memberEmail: String): Member {
        var mem : Member = Member()
        try{
            val response = memberService.getUserInfo(memberEmail)
            if(response.isSuccessful){
                mem = response.body()!!
                Log.d(TAG, "getUserInfo: $mem")
            }else{
                Log.d(TAG, "getUserInfo: 서버 통신 실패")
            }
        }catch (e: Exception){
            Log.d(TAG, "getUserInfo: 에러 발생 $e")
        }
        return mem
    }

    suspend fun updateUser(member: Member, classCode : Int, regionCode : Int, generationCode : Int) {
        viewModelScope.launch {
            try {
                memberService.updateMember(member).enqueue(object : Callback<Member> {
                    override fun onResponse(call: Call<Member>, response: Response<Member>) {
                        if (response.isSuccessful) {
                            _updateResult.value = response.body() // Assign the Member object to LiveData
                            Log.d(TAG, "onResponse: response successful, ${_updateResult.value}")
                        } else {
                            // Handle unsuccessful response here if needed
                            Log.d(TAG, "onResponse: Response is UnSuccessful -> ${response.errorBody().toString()}")
                        }
                    }

                    override fun onFailure(call: Call<Member>, t: Throwable) {
                        // Handle failure here if needed
                        Log.d(TAG, "onFailure: 호출 실패.")
                    }
                })
            } catch (e: Exception) {
                // Handle exception here if needed
            }
        }
    }

    suspend fun updateInfo(id: Int, generationCode: Int, regionCode: Int, classCode: Int): Int {
        try {
            memberService.removeMember(id)

            val classInfoResponse = memberService.setClass(id, classCode, regionCode, generationCode)
            if (classInfoResponse.isSuccessful && classInfoResponse.body()=="true") {
                Log.d(TAG, "setClass: 성공 !")
            } else {
                Log.d(TAG, "setClass: 실패 !")
                Log.d(TAG, "signedMem2: ${classInfoResponse.errorBody()}")
                val classInfoResponse2 = memberService.setClass(id, generationCode, regionCode, classCode)
                if(classInfoResponse2.isSuccessful && classInfoResponse2.body()=="true"){
                    Log.d(TAG, "updateInfo: setClass2 : 성공2 !")
                }else{
                    Log.d(TAG, "updateInfo: setClass2 : 실패2 !")
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "signedMem 에러 발생 : $e")
        }
        return id
    }



}