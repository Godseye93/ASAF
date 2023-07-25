package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Classinfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AttendenceService {

    @GET("{userid}")
    suspend fun getClassInfo(@Path("userid") userid : Int) : Response<MutableList<Classinfo>>
}