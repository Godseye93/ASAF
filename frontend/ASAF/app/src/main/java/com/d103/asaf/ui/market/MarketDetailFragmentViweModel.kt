package com.d103.asaf.ui.market

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.model.dto.MarketImage
import kotlinx.coroutines.launch

class MarketDetailFragmentViweModel : ViewModel() {

    private val _marketDetail  =  MutableLiveData<Market>()

    val marketDetail : LiveData<Market>
        get() = _marketDetail


    private val _marketImageList  = MutableLiveData<MutableList<MarketImage>>()

    val marketImageList : LiveData<MutableList<MarketImage>>
        get() = _marketImageList



    fun getMarketDetail(id : Int){
        viewModelScope.launch {

        }
    }

    fun setNullImageList(){
        val data = MarketImage(Uri.parse("https://cdn-icons-png.flaticon.com/512/75/75519.png"))
        marketImageList.value?.add(data)
    }
}