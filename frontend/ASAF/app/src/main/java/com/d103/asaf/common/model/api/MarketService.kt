package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.Member
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface MarketService {

    @Multipart
    @POST("post/register")
    suspend fun post(
        @Part("postDTO") postDTO: RequestBody,
        @Part ImageFiles: List<MultipartBody.Part>
    ) : Response<Void>
}