package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Classinfo
import com.d103.asaf.common.model.dto.Member
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AttendenceService {

    @GET("classinfo/member/{userid}")
    suspend fun getClassInfo(@Path("userid") userid : Int) : Response<MutableList<Classinfo>>

    @GET("classinfo/pro/memberIds")
    suspend fun getStudentsInfo(@Query("class_code") classCode : Int, @Query("region_code") regionCode : Int, @Query("generation_code") generationCode : Int)  : Response<MutableList<Member>>

    @GET("region/{region_code}")
    suspend fun getRegionName(@Path("region_code") regionCode: Int)  : Response<String>
}