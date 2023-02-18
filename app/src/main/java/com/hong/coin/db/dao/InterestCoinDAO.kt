package com.hong.coin.db.dao

import androidx.room.*
import com.hong.coin.db.entity.InterestCoinEntity
import kotlinx.coroutines.flow.Flow

//dao 는 쿼리

@Dao
interface InterestCoinDAO {

    //getAllData
    // https://medium.com/androiddevelopers/room-flow-273acffe5b57
    //데이터의 변경 사항을 감지하기 좋다. 변경될때 마다 ViewModel을 업뎃해야하는 번거로움을 없애준다
    @Query("SELECT * FROM interest_coin_table")
    fun getAllData() : Flow<List<InterestCoinEntity>>

    //Insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(interestCoinEntity: InterestCoinEntity)

    //Update
    //사용자가 코인 데이터를 선택했다가 다시 취소할 수도 있고, 반대로 선택 안 된 것을 선택할 수도 있게 함
    @Update
    fun update(interestCoinEntity: InterestCoinEntity)

    //getSelectedCoinList
    @Query("SELECT * FROM interest_coin_table WHERE selected = :selected")
    fun getSelectedData(selected : Boolean = true) : List<InterestCoinEntity>

}