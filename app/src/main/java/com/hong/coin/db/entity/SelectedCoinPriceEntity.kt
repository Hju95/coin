package com.hong.coin.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*


@Entity(tableName = "selected_coin_price_table")
data class SelectedCoinPriceEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val coinName: String,
    val transaction_date: String,
    val type: String,
    val units_traded: String,
    val price: String,
    val total: String,
    val timeStamp: Date

    //timeStamp 같은 타입은 DB에 바로 안들어간다 -> 컨버터 만든다


)

class DateConverters {

    @TypeConverter
    fun fromTimestamp(value : Long) : Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date : Date) : Long {
        return date.time
    }
}