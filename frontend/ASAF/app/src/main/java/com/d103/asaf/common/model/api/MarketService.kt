package com.d103.asaf.common.model.api

import androidx.room.Delete
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.MarketDetail
import com.d103.asaf.common.model.dto.Member
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface MarketService {

    @Multipart
    @POST("post/register")
    suspend fun post(
        @Part("postDTO") postDTO: RequestBody,
        @Part ImageFiles: List<MultipartBody.Part>
    ) : Response<Boolean>

    @GET("post/all")
    suspend fun getAll() : Response<MutableList<Market>>

    @GET("post/{postId}")
    suspend fun getMarket(@Path("postId") id : Long) : Response<MarketDetail>

    @DELETE("post/delete/{postId}")
    suspend fun delete(@Path("postId") id : Long) : Response<Boolean>
}