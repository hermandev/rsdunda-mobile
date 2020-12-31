package com.example.layananmandiri.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.layananmandiri.MainActivity
import com.example.layananmandiri.R
import com.example.layananmandiri.util.SharedUsers
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class  VerifikasiOtp : AppCompatActivity() {
    lateinit var code1:EditText
    lateinit var code2:EditText
    lateinit var code3:EditText
    lateinit var code4:EditText
    lateinit var code5:EditText
    lateinit var code6:EditText
    lateinit var textNomorHP:TextView
    lateinit var textKirimUlang:TextView
    lateinit var buttonVerifyOTP:MaterialButton
    lateinit var loading:ProgressBar
    lateinit var verificationId: String
    lateinit var users: SharedUsers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi_otp)
        supportActionBar?.hide()
        users = SharedUsers(this@VerifikasiOtp)
        textNomorHP = findViewById(R.id.textNomorHp)
        textNomorHP.text = "+62-${intent.getStringExtra("nomorhp")}"
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP)
        loading = findViewById(R.id.loading)
        textKirimUlang = findViewById(R.id.textKirimUlang)

        code1 = findViewById(R.id.code1)
        code2 = findViewById(R.id.code2)
        code3 = findViewById(R.id.code3)
        code4 = findViewById(R.id.code4)
        code5 = findViewById(R.id.code5)
        code6 = findViewById(R.id.code6)

        setupOTPInputs()

        textKirimUlang.setOnClickListener {
            kirimOTP()
        }

        verificationId = intent.getStringExtra("verificationId").toString()
        buttonVerifyOTP.setOnClickListener {
            if(code1.text.toString().trim().isEmpty()
                || code2.text.toString().trim().isEmpty()
                || code3.text.toString().trim().isEmpty()
                || code4.text.toString().trim().isEmpty()
                || code5.text.toString().trim().isEmpty()
                || code6.text.toString().trim().isEmpty()) {
                Toast.makeText(this@VerifikasiOtp, "Masukan kode yang benar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val code = code1.text.toString() + code2.text.toString() + code3.text.toString() + code4.text.toString() + code5.text.toString() + code6.text.toString()
            if(verificationId != null) {
                loading.visibility = View.VISIBLE
                buttonVerifyOTP.visibility = View.GONE
                    val credential = PhoneAuthProvider.getCredential(verificationId, code)
                    signInWithPhoneAuthCredential(credential)
            }
        }



    }

    private fun setupOTPInputs() {
        code1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.toString().trim().isEmpty()) {
                    code2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        code2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.toString().trim().isEmpty()) {
                    code3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        code3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.toString().trim().isEmpty()) {
                    code4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        code4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.toString().trim().isEmpty()) {
                    code5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        code5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.toString().trim().isEmpty()) {
                    code6.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    val user = task.result?.user
                    users.isAuth = true
                    users.uid = user?.uid
                    users.nomorhp = user?.phoneNumber
                    val intent = Intent(this@VerifikasiOtp, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    println( "signInWithCredential:failure ${task.exception}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {}
                }
            }
    }



    private fun kirimOTP() {
        val phoneNumberInE164 = PhoneNumberUtils.formatNumberToE164("+62${intent.getStringExtra("nomorhp")}", "ID")
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumberInE164.toString())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {

                    Toast.makeText(this@VerifikasiOtp, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(newp0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(newp0, p1)
                    verificationId = newp0
                    Toast.makeText(this@VerifikasiOtp, "Mengirim Kode OTP", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeAutoRetrievalTimeOut(p0: String) {
                    super.onCodeAutoRetrievalTimeOut(p0)
                    Toast.makeText(this@VerifikasiOtp, "ERROR CODE $p0", Toast.LENGTH_SHORT).show()
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}