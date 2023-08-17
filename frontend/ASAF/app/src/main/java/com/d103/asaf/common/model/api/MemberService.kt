package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.Member
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberService {
    // 사용자 정보를 추가한다.
//    @POST("rest/user")
    @POST("member/save")
    suspend fun insert(@Body body: Member): Boolean
    
    // 반배정
    @POST("/classinfo/create")
    suspend fun setClass(
        @Query("Id") id: Int,
        @Query("class_code") classCode: Int,
        @Query("region_code") regionCode: Int,
        @Query("generation_code") generationCode: Int
    ): Response<String>

    // 중복 확인
    @GET("member/email-check/{memberEmail}")
    suspend fun emailCheck(@Path("memberEmail") memberEmail: String): Boolean

    @GET("member/email/{memberEmail}")
    suspend fun getUserInfo(@Path("memberEmail") memberEmail: String): Response<Member>

    // 이미지 업로드 메서드
    @Multipart
    @POST("member/upload-image")
    suspend fun uploadProfileImage(
        @Part("memberEmail") memberEmail: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<Boolean>

    // 로그인
    @POST("member/login")
    suspend fun login(@Body body: Member) : Member

    @PUT("member/update")
    fun updateMember(@Body memberDTO: Member): Call<Member>

    // 로그아웃
    @GET("member/logout")
    suspend fun logout()

    // 로그인 시 토큰 추가
    @PUT("member/tokenUpdate")
    suspend fun addToken(@Query("id") id : Int, @Query("token") token : String ) : Response<Boolean>

    @DELETE("member/{memberId}")
    fun removeMember(@Path("memberId") memberId: Int): Call<Void>

    @GET("member/{memberId}")
    suspend fun getMemberInfoWithId(@Path("memberId") memberId : Int) : Response<Member>
}