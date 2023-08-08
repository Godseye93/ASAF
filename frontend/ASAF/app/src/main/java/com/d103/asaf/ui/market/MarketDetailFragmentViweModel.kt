package com.d103.asaf.ui.market

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.d103.asaf.common.model.dto.MarketDetail
import com.d103.asaf.common.model.dto.MarketImage
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.launch

class MarketDetailFragmentViweModel : ViewModel() {

    private val _marketDetail  =  MutableLiveData<MarketDetail>()

    val marketDetail : LiveData<MarketDetail>
        get() = _marketDetail


    private val _marketImageList  = MutableLiveData<MutableList<MarketImage>>()

    val marketImageList : LiveData<MutableList<MarketImage>>
        get() = _marketImageList



    fun getMarketDetail(id : Int){
        viewModelScope.launch {
            val response = RetrofitUtil.marketService.getMarket(id)
            if(response.isSuccessful){
                Log.d("마켓 상세정보", "getMarketDetail: ${response.body()}")
                _marketDetail.value = response.body()
            }
        }
    }

    fun setNullImageList(){
        val data = MarketImage(Uri.parse("https://cdn-icons-png.flaticon.com/512/75/75519.png"))
        marketImageList.value?.add(data)
    }
}