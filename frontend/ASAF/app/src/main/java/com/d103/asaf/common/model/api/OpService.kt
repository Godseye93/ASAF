package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.DocLocker
import com.d103.asaf.common.model.dto.DocSeat
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.model.dto.Member
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OpService {
    // restController 경로 확인 필요
    // 자리 배치 정보 가져오기
    @GET("/op/seat/{classCode}")
    suspend fun getSeats(@Path("classCode") classCode : Int) : Response<MutableList<DocSeat>>

    // 사물함 배치 정보 가져오기
    @GET("/op/locker/{classCode}")
    suspend fun getLockers(@Path("classCode") classCode : Int) : Response<MutableList<DocLocker>>

    // 서명 정보 가져오기
    @GET("/op/sign/{classCode}")
    suspend fun getSigns(@Path("classCode") classCode : Int) : Response<MutableList<DocSign>>

    // 반정보 가져오기
    @GET("{/op/{userId}")
    suspend fun getClasses(@Path("userId") userId : Int) : Response<MutableList<Classinfo>>

    // 자리 정보 보내기
    @POST("/op/seat")
    suspend fun postSeats(@Body seats: List<DocSeat>) : Boolean

    // 사물함 정보 보내기
    @POST("/op/locker")
    suspend fun postLockers(@Body lockers: List<DocLocker>) : Boolean

}