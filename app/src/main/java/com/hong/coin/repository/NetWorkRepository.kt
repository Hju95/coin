package com.hong.coin.repository

import com.hong.coin.network.Api
import com.hong.coin.network.Retrofitinstance

//뷰모델은 이 클래스를 보고 가져와서 사용
class NetWorkRepository {

    private val client = Retrofitinstance.getInstance().create(Api::class.java)

    suspend fun getCurrentCoinList() = client.getCurrentCoinList()

}