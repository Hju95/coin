package com.hong.coin.network

import com.hong.coin.network.model.CurrentPriceList
import retrofit2.http.GET

interface Api {

    @GET("public/ticker/ALL_KRW")
    suspend fun getCurrentCoinList() : CurrentPriceList

}