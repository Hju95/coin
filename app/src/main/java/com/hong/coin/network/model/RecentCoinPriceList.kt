package com.hong.coin.network.model

import com.hong.coin.dataModel.RecentPriceData

data class RecentCoinPriceList (

    val status : String,
    val data : List<RecentPriceData>

)