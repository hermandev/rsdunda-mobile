package com.example.layananmandiri.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.layananmandiri.MainActivity
import com.example.layananmandiri.R
import com.example.layananmandiri.util.SharedUsers

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    lateinit var users: SharedUsers
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        users = SharedUsers(this@SplashScreen)
        Handler().postDelayed({
            if(users.isAuth) {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreen, KirimOtp::class.java))
                finish()
            }
        }, 4000)
    }
}

//55942