package com.d103.asaf.ui.market

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d103.asaf.common.model.dto.Market
import com.d103.asaf.common.util.RetrofitUtil
import kotlinx.coroutines.launch

class MarketFragmentViewModel : ViewModel() {

    private val _marketList = MutableLiveData<MutableList<Market>>()
    val marketList : LiveData<MutableList<Market>>
        get() = _marketList


    fun getMarketList(){
        viewModelScope.launch {
            val response = RetrofitUtil.marketService.getAll()
            if(response.isSuccessful){
                if(!response.body().isNullOrEmpty()){
                    _marketList.value = response.body()
                    Log.d("마켓 리스트", "1 ; ${_marketList.value} ")
                }
                else{
                    _marketList.value = mutableListOf<Market>()
                }
            }
            else{
                _marketList.value = mutableListOf<Market>()
            }
        }
    }




}