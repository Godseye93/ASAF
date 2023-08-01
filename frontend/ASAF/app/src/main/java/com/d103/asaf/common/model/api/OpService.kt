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
import retrofit2.http.Query

interface OpService {
    // restController 경로 확인 필요
    // 자리 배치 정보 가져오기
    @GET("/seat")
    suspend fun getSeats(@Query("class_code") classCode : Int, @Query("region_code") regionCode : Int, @Query("generation_code") generationCode : Int) : Response<MutableList<DocSeat>>

    // 사물함 배치 정보 가져오기
    @GET("/locker")
    suspend fun getLockers(@Query("class_code") classCode : Int, @Query("region_code") regionCode : Int, @Query("generation_code") generationCode : Int) : Response<MutableList<DocLocker>>

    // 서명 정보 가져오기
    @GET("/op/sign")
    suspend fun getSigns(@Query("class_code") classCode : Int, @Query("region_code") regionCode : Int, @Query("generation_code") generationCode : Int) : Response<MutableList<DocSign>>

    // 자리 정보 보내기
    @POST("/op/seat")
    suspend fun postSeats(@Body seats: List<DocSeat>) : Boolean

    // 사물함 정보 보내기
    @POST("/op/locker")
    suspend fun postLockers(@Body lockers: List<DocLocker>) : Boolean

}