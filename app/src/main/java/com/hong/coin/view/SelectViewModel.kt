package com.hong.coin.view


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hong.coin.dataModel.CurrentPrice
import com.hong.coin.dataModel.CurrentPriceResult
import com.hong.coin.dataStore.MyDataStore
import com.hong.coin.db.entity.InterestCoinEntity
import com.hong.coin.repository.DBRepository
import com.hong.coin.repository.NetWorkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SelectViewModel : ViewModel() {


//    fun test() {
//        Timber.d("test")
//    }

    private val netWorkRepository = NetWorkRepository()
    private val dbRepository = DBRepository()

    private lateinit var currentPriceResultList : ArrayList<CurrentPriceResult>

    // 데이터변화를 관찰 LiveData
    private val _currentPriceResult = MutableLiveData<List<CurrentPriceResult>>()
    val currentPriceResult : LiveData<List<CurrentPriceResult>>
        get() = _currentPriceResult

    //만약 데이터가 100만개인데 10만개만 저장되고 메인으로 넘어간다면?
    //저장이 다 됐는지 확인하는 걸 추가
    private val _saved = MutableLiveData<String>()
    val save : LiveData<String>
        get() = _saved

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

    //DB에 데이터 저장
    //https://developer.android.com/kotlin/coroutines/coroutines-adv?hl=ko
    fun saveSelectedCoinList(selectedCoinList: ArrayList<String>) = viewModelScope.launch(Dispatchers.IO){

        //1.전체 코인 데이터를 가져와서
        for(coin in currentPriceResultList) {
            Timber.d(coin.toString())

            //2.내가 선택한 코인인지 아닌지 구분해서
            //포함하면 TRUE / 포함하지 않으면 FALSE
            val selected = selectedCoinList.contains(coin.coinName)

            val interestCoinEntity = InterestCoinEntity(
                0,
                coin.coinName,
                coin.coinInfo.opening_price,
                coin.coinInfo.closing_price,
                coin.coinInfo.min_price,
                coin.coinInfo.max_price,
                coin.coinInfo.units_traded,
                coin.coinInfo.acc_trade_value,
                coin.coinInfo.prev_closing_price,
                coin.coinInfo.units_traded_24H,
                coin.coinInfo.acc_trade_value_24H,
                coin.coinInfo.fluctate_24H,
                coin.coinInfo.fluctate_rate_24H,
                selected)

            //3.저장
            interestCoinEntity.let {
                dbRepository.insertInterestCoinData(it)
            }
        }

        //이렇게 추가해줘야 에러안남 
        withContext(Dispatchers.Main){
            //아래줄만 쓰면 에러남
            _saved.value = "done"
        }

    }
}