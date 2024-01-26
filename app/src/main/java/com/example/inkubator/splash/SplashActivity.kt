package com.example.inkubator.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.inkubator.R
import com.example.inkubator.main.MainActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(500)
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        lifecycleScope.cancel()
        super.onPause()
    }
}