package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Noti
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NotiService {

    @POST("")
    suspend fun pushMessage(@Body message : Noti) : Response<Boolean>

}