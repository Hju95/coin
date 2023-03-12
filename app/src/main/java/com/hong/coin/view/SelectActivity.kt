package com.hong.coin.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.hong.coin.background.GetCoinPriceRecentContractWorkManager
import com.hong.coin.databinding.ActivitySelectBinding
import com.hong.coin.main.MainActivity
import com.hong.coin.view.adapter.SelectRVAdapter
import timber.log.Timber
import java.util.concurrent.TimeUnit

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

                // 가장 처음으로 우리가 저장한 코인 정보가 시작되는 지점
                saveInterestCoinDataPeriodic()
            }
        })


    }

    private fun saveInterestCoinDataPeriodic() {
        val myWork = PeriodicWorkRequest.Builder(
            GetCoinPriceRecentContractWorkManager::class.java,
            15,
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "GetCoinPriceRecentContractedWorkManager",
            ExistingPeriodicWorkPolicy.KEEP,
            myWork
        )
    }

}