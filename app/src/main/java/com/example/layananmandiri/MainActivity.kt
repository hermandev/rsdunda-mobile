package com.example.layananmandiri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.layananmandiri.activity.FormProfile
import com.example.layananmandiri.api.CloudApi
import com.example.layananmandiri.models.users.Users
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)
    }


    private fun checkUsers(uid:String, phoneNumber:String) {
        CloudApi().checkUsers(uid).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if(response.body()?.success!!) {
//                    val intent = Intent(this@MainActivity, MainActivity::class.java)
//                    intent.putExtra("fullname", response.body()?.data?.fullname)
//                    intent.putExtra("nik", response.body()?.data?.nik)
//                    startActivity(intent)
                } else {
//                    val intent = Intent(this@VerifikasiOtp, FormProfile::class.java)
//                    intent.putExtra("nomorhp", phoneNumber)
//                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {}
        })
    }
}