package com.hong.coin.view


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hong.coin.dataModel.CurrentPrice
import com.hong.coin.dataModel.CurrentPriceResult
import com.hong.coin.dataStore.MyDataStore
import com.hong.coin.repository.NetWorkRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectViewModel : ViewModel() {


//    fun test() {
//        Timber.d("test")
//    }

    private val netWorkRepository = NetWorkRepository()

    private lateinit var currentPriceResultList : ArrayList<CurrentPriceResult>

    // 데이터변화를 관찰 LiveData
    private val _currentPriceResult = MutableLiveData<List<CurrentPriceResult>>()
    val currentPriceResult : LiveData<List<CurrentPriceResult>>
    get() = _currentPriceResult


    fun getCurrentCoinList() = viewModelScope.launch {

        val result = netWorkRepository.getCurrentCoinList()

//        Timber.d(result.toString())
        currentPriceResultList = ArrayList()

        for(coin in result.data) {

            try {
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data.get(coin.key))
                val gsonFromJson = gson.fromJson(gsonToJson, CurrentPrice::class.java)

                val currentPriceResult = CurrentPriceResult(coin.key, gsonFromJson)

//                Timber.d(currentPriceResult.toString())
                currentPriceResultList.add(currentPriceResult)
            } catch (e : java.lang.Exception) {
                Timber.d(e.toString())
            }

        }

        _currentPriceResult.value = currentPriceResultList
    }

    fun setUpFirstFlag() = viewModelScope.launch {
        MyDataStore().setupFirstData()
    }

}