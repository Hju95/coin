package com.hong.coin.main

import androidx.lifecycle.*
import com.hong.coin.dataModel.UpDownDataSet
import com.hong.coin.db.entity.InterestCoinEntity
import com.hong.coin.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val dbRepository = DBRepository()

    lateinit var selectedCoinList : LiveData<List<InterestCoinEntity>>

    //2000분 전 이러면 livedata 사용하면 안될 것 같은데.. 왜? 알아보자
    private val _arr15min = MutableLiveData<List<UpDownDataSet>>()
    val arr15min : LiveData<List<UpDownDataSet>>
        get() = _arr15min

    private val _arr30min = MutableLiveData<List<UpDownDataSet>>()
    val arr30min : LiveData<List<UpDownDataSet>>
        get() = _arr30min

    private val _arr45min = MutableLiveData<List<UpDownDataSet>>()
    val arr45min : LiveData<List<UpDownDataSet>>
        get() = _arr45min

    //CoinListFragment

    fun getAllInterestCoinData() = viewModelScope.launch {

        val coinList = dbRepository.getAllInterestCoinData().asLiveData()
        selectedCoinList = coinList

    }

    fun updateInterestCoinData(interestCoinEntity: InterestCoinEntity) = viewModelScope.launch(Dispatchers.IO) {
        if (interestCoinEntity.selected) {
            interestCoinEntity.selected = false
        } else {
            interestCoinEntity.selected = true
        }

        dbRepository.updateInterestCoinData(interestCoinEntity)
    }

    //PriceChangeFragment
    //1. 관심있다고 선택한 코인 리스트를 가져와서
    //2. 관심있다고 선택한 코인 리스트를 반복문을 통해 하나씩 가져와서
    //3. 저장된 코인 가격 리스트를 가져와서
    //4. 시간대마다 어떻게 변경되었는지를 알려주는 로직을 작성
    fun getAllSelectedCoinData() = viewModelScope.launch(Dispatchers.IO) {

        //1. 관심있다고 선택한 코인 리스트를 가져와서
        val selectedCoinList = dbRepository.getAllInterestSelectedCoinData()

        val arr15min = ArrayList<UpDownDataSet>()
        val arr30min = ArrayList<UpDownDataSet>()
        val arr45min = ArrayList<UpDownDataSet>()

        //2. 관심있다고 선택한 코인 리스트를 반복문을 통해 하나씩 가져와서
        for (data in selectedCoinList) {

            //3. 저장된 코인 가격 리스트를 가져와서
            val coinName = data.coin_name // coinName = BTC
            val oneCoinData = dbRepository.getOneSelectedCoinData(coinName).reversed() //마지막이 최신값임으로 거꾸로 가져옴

            // [0 1 2 3 4] -> 가장 마지막에 저장된 값이 최신 값

            val size = oneCoinData.size

            if(size > 1){
                // DB에 값이 2개 이상은 있다.
                val changedPrice = oneCoinData[0].price.toDouble() - oneCoinData[1].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr15min.add(upDownDataSet)
            }

            if(size > 2){
                // DB에 값이 3개 이상은 있다.
                val changedPrice = oneCoinData[0].price.toDouble() - oneCoinData[2].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr30min.add(upDownDataSet)
            }

            if(size > 3){
                // DB에 값이 4개 이상은 있다.
                val changedPrice = oneCoinData[0].price.toDouble() - oneCoinData[3].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr45min.add(upDownDataSet)
            }
        }

        //그래서 변경
        withContext(Dispatchers.Main) {

            //이대로 쓰면 앱 죽음 .. 백그라운드에서 쓰레드를 쓰면 안된다
            _arr15min.value = arr15min
            _arr30min.value = arr30min
            _arr45min.value = arr45min
        }
    }
}