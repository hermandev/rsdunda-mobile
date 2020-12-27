package com.example.layananmandiri.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.layananmandiri.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class KirimOtp : AppCompatActivity() {
    lateinit var loading:ProgressBar
    lateinit var buttonGetOtp:MaterialButton
    lateinit var nomorHP:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kirim_otp)
        supportActionBar?.hide()


        loading = findViewById(R.id.loading)
        nomorHP = findViewById(R.id.etNomorHp)
        buttonGetOtp = findViewById(R.id.buttonGetOTP)




        buttonGetOtp.setOnClickListener {
            if(nomorHP.text.trim().isEmpty()) {
                Toast.makeText(this@KirimOtp, "Masukan Nomor HP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loading.visibility = View.VISIBLE
            buttonGetOtp.visibility = View.GONE
            kirimOTP()
        }
    }

    private fun kirimOTP() {
        val phoneNumberInE164 = PhoneNumberUtils.formatNumberToE164("+62${nomorHP.text}", "ID")
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumberInE164.toString())
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    loading.visibility = View.GONE
                    buttonGetOtp.visibility = View.VISIBLE
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    loading.visibility = View.GONE
                    buttonGetOtp.visibility = View.VISIBLE
                    Toast.makeText(this@KirimOtp, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    loading.visibility = View.GONE
                    buttonGetOtp.visibility = View.VISIBLE
                    val intent = Intent(this@KirimOtp, VerifikasiOtp::class.java)
                    intent.putExtra("nomorhp", nomorHP.text.toString())
                    intent.putExtra("verificationId", p0)
                    startActivity(intent)
                }

                override fun onCodeAutoRetrievalTimeOut(p0: String) {
                    super.onCodeAutoRetrievalTimeOut(p0)
                    loading.visibility = View.GONE
                    buttonGetOtp.visibility = View.VISIBLE
                    Toast.makeText(this@KirimOtp, "ERROR CODE $p0", Toast.LENGTH_SHORT).show()
                }
            }).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onResume() {
        super.onResume()
        loading.visibility = View.GONE
        buttonGetOtp.visibility = View.VISIBLE
    }
}