package com.example.layananmandiri.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import coil.load
import com.example.layananmandiri.MainActivity
import com.example.layananmandiri.R
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class Pendaftaran : AppCompatActivity() {
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran)
        supportActionBar?.title = "Pendaftaran"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        actionBar?.title = "Pendaftaran"
        val extras = intent.extras
        val poli_tujuan = findViewById<Spinner>(R.id.poli_tujuan)
        val txt_input_tanggal = findViewById<EditText>(R.id.txt_input_tanggal)
        val txt_pasien_nama = findViewById<TextView>(R.id.txt_pasien_nama)
        val txt_pasien_norm = findViewById<TextView>(R.id.txt_pasien_norm)
        val photo_pasien = findViewById<ImageView>(R.id.photo_pasien)

//        txt_input_tanggal.setText("" + day + "/" + month +1 + "/" + year)
        txt_pasien_nama.text = extras?.getString("NAMA")
        txt_pasien_norm.text = "NORM : ${extras?.getString("NORM")}";
        if(extras?.getString("JENIS_KELAMIN") == "1") {
            photo_pasien.load(R.drawable.ic_pasien_pria)
        } else {
            photo_pasien.load(R.drawable.ic_pasien_wanita)
        }

        txt_input_tanggal.setOnClickListener {
            showDatePicker()
        }



    }


    private fun showDatePicker() {
        val txt_input_tanggal = findViewById<EditText>(R.id.txt_input_tanggal)
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate.set(year,monthOfYear, dayOfMonth)
            txt_input_tanggal.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)
        }, year, month, day)
        datePickerDialog.show()
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