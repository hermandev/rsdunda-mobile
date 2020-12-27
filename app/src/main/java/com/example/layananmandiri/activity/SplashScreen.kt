package com.example.layananmandiri.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.layananmandiri.MainActivity
import com.example.layananmandiri.R

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, KirimOtp::class.java))
            finish()
        }, 4000)
    }
}

//55942