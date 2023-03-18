package com.hong.coin.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hong.coin.R
import com.hong.coin.dataModel.UpDownDataSet

class PriceListUpDownRVAdapter (val context : Context, val dataSet : List<UpDownDataSet>)
    : RecyclerView.Adapter<PriceListUpDownRVAdapter.ViewHolder>(){

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val coinName = view.findViewById<TextView>(R.id.coinName)
        val coinPriceUpDown = view.findViewById<TextView>(R.id.coinPriceUpDown)
        val price = view.findViewById<TextView>(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coin_price_change_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //음.. 관심있는 코인 취소했다 다시하면 사라지네.. 바로 뜨게끔 하는 방법 찾아보기
        holder.coinName.text = dataSet[position].coinName

        if(dataSet[position].upDownPrice.contains("-")){
            holder.coinPriceUpDown.text = "하락"
            holder.coinPriceUpDown.setTextColor(Color.parseColor("#114fed"))
        } else {
            holder.coinPriceUpDown.text = "상승"
            holder.coinPriceUpDown.setTextColor(Color.parseColor("#ed2e11"))
        }

        holder.price.text = dataSet[position].upDownPrice.split(".")[0]
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}