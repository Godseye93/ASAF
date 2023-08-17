package com.d103.asaf.ui.market

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.config.ApplicationClass

import com.d103.asaf.common.model.dto.MarketDetail
import com.d103.asaf.common.model.dto.MarketImage
import com.d103.asaf.common.model.dto.Member
import com.d103.asaf.common.util.RetrofitUtil
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MarketDetailFragmentViweModel : ViewModel() {

    private val _marketDetail  =  MutableLiveData<MarketDetail>()

    val marketDetail : LiveData<MarketDetail>
        get() = _marketDetail


    private val _marketImageList  = MutableLiveData<MutableList<MarketImage>>()

    val marketImageList : LiveData<MutableList<MarketImage>>
        get() = _marketImageList




    private val _marketUserInfo = MutableLiveData<Member>()
    val marketUserInfo : LiveData<Member>
        get() = _marketUserInfo

    fun getMarketDetail(id : Int){
        viewModelScope.launch {
            val response = RetrofitUtil.marketService.getMarket(id.toLong())
            if(response.isSuccessful){
                Log.d("마켓 상세정보", "getMarketDetail: ${response.body()}")
                _marketDetail.value = response.body()
            }
        }
    }

    fun delete(){
        viewModelScope.launch {
            val response = marketDetail.value?.id?.let { RetrofitUtil.marketService.delete(it.toLong()) }
            if (response != null) {
                if(response.isSuccessful){
                    if(response.body()!!){
                        Log.d("삭제", "delete: 삭제 성공")
                    } else{
                        Log.d("삭제", "delete: 삭제 실패")
                    }
                }
            }
        }
    }

    fun getUserInfo(userId : Int){
        viewModelScope.launch {
            val response = RetrofitUtil.memberService.getMemberInfoWithId(userId)
            if(response.isSuccessful){
                val responseBody = response.body()
                _marketUserInfo.value = responseBody!!
            }
        }
    }


}