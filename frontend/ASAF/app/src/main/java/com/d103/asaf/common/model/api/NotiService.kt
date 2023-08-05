package com.d103.asaf.common.model.api

import com.d103.asaf.common.model.dto.Noti
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotiService {

    @POST("notice/immediate")
    suspend fun pushMessage(@Body message : List<Noti>) : Response<Boolean>

    @POST("notice")
    suspend fun pushMessageReservation(@Body message : List<Noti>) : Response<Boolean>

    @GET("notice/getBySenderIdAndDate")
    suspend fun getTodayMessage(@Query("sender") sender : Int, @Query("registerTime") registerTime : Long) : Response<MutableList<Noti>>


    @DELETE("notice/{id}")
    suspend fun deleteNoti(@Path("id") id : Int) : Response<Boolean>

    @PUT("notice")
    suspend fun updateNoti(@Body noti : Noti) : Response<Boolean>
}