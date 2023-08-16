package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.DocLocker
import com.d103.asaf.common.model.dto.DocSeat
import com.d103.asaf.common.model.dto.DocSign
import com.d103.asaf.common.model.dto.Member
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface OpService {
    // restController 경로 확인 필요
    // 반 별 자리 배치 정보 가져오기
    @GET("/seat/classCodes")
    suspend fun getSeats(@Query("class_code") classCode : Int, @Query("region_code") regionCode : Int, @Query("generation_code") generationCode : Int) : Response<MutableList<DocSeat>>

    // 각 자리 정보 가져오기
    @GET("/seat/user")
    suspend fun getSeat(@Query("class_code") classCode : Int, @Query("region_code") regionCode : Int, @Query("generation_code") generationCode : Int, @Query("id") userId : Int) : Response<DocSeat>

    // 사물함 배치 정보 가져오기
    @GET("/locker/classCodes")
    suspend fun getLockers(@Query("class_code") classCode : Int, @Query("region_code") regionCode : Int, @Query("generation_code") generationCode : Int) : Response<MutableList<DocLocker>>

    // 서명 정보 가져오기
    @GET("/sign/classCodes")
    suspend fun getSigns(@Query("class_code") classCode : Int, @Query("region_code") regionCode : Int, @Query("generation_code") generationCode : Int, @Query("month") month : String) : Response<MutableList<DocSign>>

    // 자리 정보 보내기
    @POST("/seat/complete")
    suspend fun postSeats(@Body seats: List<DocSeat>) : Response<Boolean>

    // 사물함 정보 보내기
    @POST("/locker/complete")
    suspend fun postLockers(@Body lockers: List<DocLocker>) : Response<Boolean>

    // 서명 정보 보내기
    @Multipart
    @POST("/sign/upload-image")
    suspend fun postSigns(
        @Part("signDTO") sign: RequestBody,
        @Part ImageFile: MultipartBody.Part
    ): Response<Boolean>
}