package com.hong.coin.view.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hong.coin.MainActivity
import com.hong.coin.databinding.ActivityIntroBinding
import timber.log.Timber

//Splash 화면 만들기
//handle => 3초 뒤에 다른액티비티(MainActivity로 이동)

class IntroActivity : AppCompatActivity() {

    private  lateinit var binding : ActivityIntroBinding
    private val viewModel : IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.d("onCreate")

        viewModel.checkFirstFlag()
        viewModel.first.observe(this, {
            if (it) {
                //처음 접속하는 유저가 아님

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                //처음 접속하는 유저
                binding.animationView.visibility = View.INVISIBLE
                binding.fragmentContainerView.visibility = View.VISIBLE
            }
        })
    }
}