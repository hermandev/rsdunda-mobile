package com.example.layananmandiri.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.layananmandiri.MainActivity
import com.example.layananmandiri.R
import com.example.layananmandiri.api.CloudApi
import com.example.layananmandiri.models.users.Users
import com.example.layananmandiri.util.SharedUsers
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class KirimOtp : AppCompatActivity() {
    lateinit var loading:ProgressBar
    lateinit var buttonGetOtp:MaterialButton
    lateinit var nomorHP:EditText
    lateinit var users:SharedUsers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kirim_otp)
        supportActionBar?.hide()

        users = SharedUsers(this@KirimOtp)
        loading = findViewById(R.id.loading)
        nomorHP = findViewById(R.id.etNomorHp)
        buttonGetOtp = findViewById(R.id.buttonGetOTP)

        checkUser()


        buttonGetOtp.setOnClickListener {
            if(nomorHP.text.trim().isEmpty()) {
                Toast.makeText(this@KirimOtp, "Masukan Nomor HP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loading.visibility = View.VISIBLE
            buttonGetOtp.visibility = View.GONE
            kirimOTP()
//            users.isAuth = true
//            users.profilCheck = true
//            users.uid = "qxldmSLp5Pcfc2iZktGWwuwgDBm2"
//            val intent = Intent(this@KirimOtp, MainActivity::class.java)
//            intent.putExtra("uid", "qxldmSLp5Pcfc2iZktGWwuwgDBm2")
//            intent.putExtra("nomorhp", "+6285398104825")
//            startActivity(intent)
//            finish()
        }
    }

    private fun kirimOTP() {
        val phoneNumberInE164 = PhoneNumberUtils.formatNumberToE164("+62${nomorHP.text}", "ID")
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumberInE164.toString())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    loading.visibility = View.GONE
                    buttonGetOtp.visibility = View.VISIBLE
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    loading.visibility = View.GONE
                    buttonGetOtp.visibility = View.VISIBLE
                    Toast.makeText(this@KirimOtp, "Mohon maaf anda sudah mengirim Kode OTP terlalu banyak. silahkan coba kembali besok!", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    loading.visibility = View.GONE
                    buttonGetOtp.visibility = View.VISIBLE
                    users.nomorhp = nomorHP.text.toString()
                    val intent = Intent(this@KirimOtp, VerifikasiOtp::class.java)
                    intent.putExtra("nomorhp", nomorHP.text.toString())
                    intent.putExtra("verificationId", p0)
                    startActivity(intent)
                    finish()
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
                    val intent = Intent(this@KirimOtp, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    return;
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
//                mAlertDialog.dismiss()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loading.visibility = View.GONE
        buttonGetOtp.visibility = View.VISIBLE
    }
}