package com.example.layananmandiri.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.layananmandiri.MainActivity
import com.example.layananmandiri.R

class Pendaftaran : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran)
        supportActionBar?.title = "Pendaftaran"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        actionBar?.title = "Pendaftaran"


//        val dialog = Dialog(this)
//        dialog.setContentView(R.layout.pop_up_daftar)
//        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        dialog.setCancelable(false)
//        dialog.
//            val btn_popup_norm = findViewById<MaterialButton>(R.id.btn_popup_norm)
//            val btn_popup_nik = findViewById<MaterialButton>(R.id.btn_popup_nik)
//
//            btn_popup_norm?.setOnClickListener {
////                popupNORM()
//                print("nik KLIK")
//            }
//
//            btn_popup_nik?.setOnClickListener {
////                popupNIK()
//                print("nik KLIK")
//            }
//            dialog?.show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@Pendaftaran, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}