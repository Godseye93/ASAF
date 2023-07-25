package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Member
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberService {
    // 사용자 정보를 추가한다.
//    @POST("rest/user")
    @POST("member/save")
    suspend fun insert(@Body body: Member): Boolean

    // request parameter로 전달된 id가 이미 사용중인지 반환한다.
    @GET("rest/user/isUsed")
    suspend fun isUsedId(@Query("id") id: String): Boolean

}