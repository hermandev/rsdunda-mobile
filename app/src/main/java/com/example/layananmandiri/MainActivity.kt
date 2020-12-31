package com.example.layananmandiri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.layananmandiri.activity.FormProfile
import com.example.layananmandiri.activity.KirimOtp
import com.example.layananmandiri.api.CloudApi
import com.example.layananmandiri.models.users.Users
import com.example.layananmandiri.util.SharedUsers
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var users:SharedUsers
    lateinit var uid:String
    lateinit var nomorhp:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        uid = intent.getStringExtra("uid").toString()
        nomorhp = intent.getStringExtra("nomorhp").toString()
        users = SharedUsers(this@MainActivity)
        checkUser()

        if(users.profilCheck) {
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
            val navController = findNavController(R.id.fragment)
            bottomNavigationView.setupWithNavController(navController)
        } else {
            checkUser()
            val intent = Intent(this@MainActivity, KirimOtp::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkUser() {
        CloudApi().checkUsers(users.uid.toString()).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if(response.body()?.success == true) {
                    users.nomorhp = response.body()?.data?.nohp
                    users.nik = response.body()?.data?.nik
                    users.norm = response.body()?.data?.norm
                    users.fullname = response.body()?.data?.fullname
                    users.alamat = response.body()?.data?.alamat
                    users.kelamin = response.body()?.data?.kelamin
                    users.foto = response.body()?.data?.foto
                    users.uid = response.body()?.data?.session
                    users.profilCheck = true
                } else {
                    val intent = Intent(this@MainActivity, FormProfile::class.java)
                    intent.putExtra("nomorhp", nomorhp)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
//                mAlertDialog.dismiss()
            }
        })
    }


}