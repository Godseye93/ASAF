package com.d103.asaf.ui.market

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.MarketImage
import com.d103.asaf.common.util.RetrofitUtil
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

private const val TAG = "MarketRegisterFragmentV ASAF"
class MarketRegisterFragmentViewModel : ViewModel() {

     var photoRegisterList = mutableListOf<MarketImage>()
     var photoIamgeFileList = mutableListOf<MultipartBody.Part>()
     lateinit var marketInfo : Market

    fun createMarketRequestBody(market: Market): RequestBody {
        val json = Gson().toJson(market)
        return json.toRequestBody("application/json".toMediaTypeOrNull())
    }

     fun post(){

         Log.d("마켓 Post", "$marketInfo, $photoIamgeFileList ")
          viewModelScope.launch {
              val response = RetrofitUtil.marketService.post(createMarketRequestBody(marketInfo), photoIamgeFileList)
               if(response.isSuccessful){
                   Log.d(TAG, "post: ${response.body()} ")

               }
              else{
                   Log.d(TAG, "post: ${response}")
              }

          }


     }

}