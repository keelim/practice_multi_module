package com.keelim.commonlibrary

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.keelim.commonlibrary.databinding.ActivityBaseSplashBinding

class BaseSplashActivity : AppCompatActivity(R.layout.activity_base_splash) {
    private lateinit var binding: ActivityBaseSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseSplashBinding.inflate(layoutInflater)

        when (AppManager.appType) {
            AppManager.AppType.keelchat -> {
                binding.baseContainer.setBackgroundColor(Color.BLACK)
                binding.baseTextView.text = "Welcome! \n KeelChat"
                binding.baseTextView.setTextColor(Color.WHITE)
            }

            AppManager.AppType.keelstudy ->{
                binding.baseContainer.setBackgroundColor(Color.BLUE)
                binding.baseTextView.text = "Welcome! \n KeelStudy"
                binding.baseTextView.setTextColor(Color.WHITE)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 3000)

    }
}