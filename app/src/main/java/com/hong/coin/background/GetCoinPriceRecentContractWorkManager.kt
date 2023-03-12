package com.hong.coin.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hong.coin.repository.DBRepository
import com.hong.coin.repository.NetWorkRepository
import timber.log.Timber


// 최근 거래된 코인 가격 내역을 가져오는 WorkManager

//1.저희가 관심있어하는 코인 리스트를 가져와서 __SelectActivity 에 해당 리스트 있다.
//2.관심있는 코인 각각의 가격 변동 정보를 가져와서 (New API)
//3.관심있는 코인 각각의 가격 변동 정보를 DB에 저장장
class GetCoinPriceRecentContractWorkManager(val context : Context, workerParameters: WorkerParameters)
    : CoroutineWorker(context, workerParameters) {

    private val dbRepository = DBRepository()
    private val netWorkRepository = NetWorkRepository()

    override suspend fun doWork(): Result {

        Timber.d("doWork")

        getAllInterestSelectedCoinData()

        return Result.success()

    }

    //1.관심있어 하는 코인 리스트를 가져와서
    suspend fun getAllInterestSelectedCoinData() {
        val selectedCoinList = dbRepository.getAllInterestSelectedCoinData()

        for(coinData in selectedCoinList) {
            Timber.d(coinData.toString())

            //2.관심있는 코인 각각의 가격 변동 정보를 가져와서 ( New API )
            val recentCoinPriceList = netWorkRepository.getInterestCoinPriceData(coinData.coin_name)

            Timber.d(recentCoinPriceList.toString())
        }
    }


}