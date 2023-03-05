package com.hong.coin.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hong.coin.main.MainActivity
import com.hong.coin.databinding.ActivitySelectBinding
import com.hong.coin.view.adapter.SelectRVAdapter
import timber.log.Timber

class SelectActivity : AppCompatActivity() {

    private  lateinit var binding : ActivitySelectBinding
    private val viewModel : SelectViewModel by viewModels()

    private lateinit var selectRVAdapter: SelectRVAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel.test()

        viewModel.getCurrentCoinList()
        viewModel.currentPriceResult.observe(this, {

            selectRVAdapter = SelectRVAdapter(this, it)

            binding.coinListRV.adapter = selectRVAdapter
            binding.coinListRV.layoutManager = LinearLayoutManager(this)
            Timber.d(it.toString())
        })


        binding.laterTextArea.setOnClickListener {

            viewModel.setUpFirstFlag()
            viewModel.saveSelectedCoinList(selectRVAdapter.selectedCoinList)

        }

        //Room, SQLlite 를 사용하지 않고 DataStore 을 사용하는 이유는 굳이 DB까지는 아니여서..
        //DataStore 은 Flag ON/OFF 저장할 때 좋더라,, 처음가입한 사람인가? 설정을 ON/OFF

        viewModel.save.observe(this, {
            if(it.equals("done")){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })


    }
}