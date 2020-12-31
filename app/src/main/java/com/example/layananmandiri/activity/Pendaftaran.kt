package com.example.layananmandiri.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import coil.load
import com.example.layananmandiri.MainActivity
import com.example.layananmandiri.R
import com.example.layananmandiri.api.CloudApi
import com.example.layananmandiri.models.pendaftaran.Pendaftaran
import com.example.layananmandiri.models.pendaftaran.PendaftaranRequest
import com.example.layananmandiri.util.SharedUsers
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Pendaftaran : AppCompatActivity() {
    private val call = Calendar.getInstance()
    lateinit var poli_tujuan:Spinner
    lateinit var keperluan_tujuan:Spinner
    lateinit var jenis_rujukan:RadioGroup
    lateinit var data_rujukan:RadioGroup
    lateinit var txt_input_tanggal:EditText
    lateinit var txt_pasien_nama:TextView
    lateinit var txt_pasien_norm:TextView
    lateinit var photo_pasien:ImageView
    lateinit var no_bpjs:EditText
    lateinit var input_nik:EditText
    lateinit var ln_jenis_rujukan:LinearLayout
    lateinit var surat_rujukan_baru:MaterialCardView
    lateinit var resume_kontrol:MaterialCardView
    lateinit var rujukan_terakhir:MaterialCardView
    lateinit var btnDaftarkan:MaterialButton
    lateinit var users: SharedUsers



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran)
        supportActionBar?.title = "Formulir Pendaftaran"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        actionBar?.title = "Formulir Pendaftaran"
        users = SharedUsers(this@Pendaftaran)
        val extras = intent.extras
        btnDaftarkan = findViewById(R.id.btnDaftarkan)
        keperluan_tujuan = findViewById(R.id.keperluan_tujuan)
        poli_tujuan = findViewById(R.id.poli_tujuan)
        txt_input_tanggal = findViewById(R.id.txt_input_tanggal)
        txt_pasien_nama = findViewById(R.id.txt_pasien_nama)
        txt_pasien_norm = findViewById(R.id.txt_pasien_norm)
        input_nik = findViewById(R.id.input_nik)
        photo_pasien = findViewById(R.id.photo_pasien)
        jenis_rujukan = findViewById(R.id.jenis_rujukan)
        data_rujukan = findViewById(R.id.data_rujukan)
        no_bpjs = findViewById(R.id.no_bpjs)
        ln_jenis_rujukan = findViewById(R.id.ln_jenis_rujukan)
        surat_rujukan_baru = findViewById(R.id.surat_rujukan_baru)
        resume_kontrol = findViewById(R.id.resume_kontrol)
        rujukan_terakhir = findViewById(R.id.rujukan_terakhir)

        txt_pasien_nama.text = extras?.getString("NAMA")
        txt_pasien_norm.text = "NORM : ${extras?.getString("NORM")}"
        if(extras?.getString("JENIS_KELAMIN") == "1") {
            photo_pasien.load(R.drawable.ic_pasien_pria)
        } else {
            photo_pasien.load(R.drawable.ic_pasien_wanita)
        }

        val dataSetListiner = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                call.set(Calendar.YEAR, year)
                call.set(Calendar.MONTH, month)
                call.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDatePicker()
            }
        }

        txt_input_tanggal.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                DatePickerDialog(
                    this@Pendaftaran,
                    dataSetListiner,
                    call.get(Calendar.YEAR),
                    call.get(Calendar.MONTH),
                    call.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        })

        val poliklinik = resources.getStringArray(R.array.Poliklinik)
        val keperluan = resources.getStringArray(R.array.Keperluan)
        val adapterPoliklinik = ArrayAdapter(this@Pendaftaran, android.R.layout.simple_spinner_dropdown_item, poliklinik)
        val adapterKeperluan = ArrayAdapter(this@Pendaftaran, android.R.layout.simple_spinner_dropdown_item, keperluan)
        poli_tujuan.adapter = adapterPoliklinik
        keperluan_tujuan.adapter = adapterKeperluan

        // check checked jenis rujukan
        jenis_rujukan.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                var rButton = findViewById<RadioButton>(checkedId)
                if(rButton.text == "BPJS") {
                    no_bpjs.visibility = View.VISIBLE
                    ln_jenis_rujukan.visibility = View.VISIBLE
                    surat_rujukan_baru.visibility = View.VISIBLE
                } else {
                    no_bpjs.visibility = View.GONE
                    surat_rujukan_baru.visibility = View.GONE
                    ln_jenis_rujukan.visibility = View.GONE
                }
            }
        })

        data_rujukan.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                var rButton = findViewById<RadioButton>(checkedId)
                if(rButton.text == "BARU") {
                    surat_rujukan_baru.visibility = View.VISIBLE
                    resume_kontrol.visibility = View.GONE
                    rujukan_terakhir.visibility = View.GONE
                } else {
                    surat_rujukan_baru.visibility = View.GONE
                    resume_kontrol.visibility = View.VISIBLE
                    rujukan_terakhir.visibility = View.VISIBLE
                }
            }
        })

        btnDaftarkan.setOnClickListener {
            var RUJUKAN = ""
            var JENIS = ""
            var NO_BPJS = ""
            var nama = extras?.getString("NAMA")
            val norm = extras?.getString("NORM")
            val tgl_lahir = extras?.getString("TANGGAL_LAHIR")
            val nik =  input_nik.text.toString()
            val tanggal = txt_input_tanggal.text.toString()
            val poli = poli_tujuan.selectedItem.toString()
            val keperluan = keperluan_tujuan.selectedItem.toString()
            if(nik == "") {
                Toast.makeText(this@Pendaftaran, "Nik belum di isi", Toast.LENGTH_SHORT).show()
                input_nik.isFocusable = true
                return@setOnClickListener
            }

            if(tanggal == "") {
                Toast.makeText(this@Pendaftaran, "Tanggal rencana pemeriksaan harus di isi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(poli == "-- pilih --") {
                Toast.makeText(this@Pendaftaran, "Silahkan pilih poli tujuan anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(keperluan == "-- pilih --") {
                Toast.makeText(this@Pendaftaran, "Silahkan pilih keperluan anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var rujukanChecked = jenis_rujukan.checkedRadioButtonId
            if(rujukanChecked != -1) {
                var rujukan  = findViewById<RadioButton>(rujukanChecked)
                if(rujukan.text == "BPJS") {
                    RUJUKAN = "BPJS"
                    NO_BPJS = no_bpjs.text.toString()
                    if(NO_BPJS == "") {
                        Toast.makeText(this@Pendaftaran, "NO BPJS belum di isi", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    var dRujukan = data_rujukan.checkedRadioButtonId
                    if(dRujukan != -1) {
                        var jRujukan  = findViewById<RadioButton>(rujukanChecked)
                        if(jRujukan.text == "BARU") {
                            JENIS = "BARU"
                        } else {
                            JENIS = "LAMA"
                        }
                    } else {
                        Toast.makeText(this@Pendaftaran, "Silahkan pilih jenis rujukan anda", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                } else {
                    RUJUKAN = "UMUM"
                }
            } else {
                Toast.makeText(this@Pendaftaran, "Silahkan pilih rujukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val reqPendaftaran = PendaftaranRequest(
                TGL_BOOKING = tanggal,
                NORM = norm!!,
                NIK = nik,
                TANGGAL_LAHIR = tgl_lahir!!,
                POLI = poli,
                KEPESERTAAN = RUJUKAN,
                KEPERLUAN = keperluan,
                JENIS_RUJUKAN = JENIS,
                NO_BPJS = NO_BPJS,
                NO_RUJUKAN = "",
                FOTO_KTP = "",
                FOTO_RUJUKAN = "",
                FOTO_KONTROL = "",
                STATUS_BPJS = "",
                NO_SEP = "",
                STATUS_PELAYANAN = "",
                STATUS = "",
                session = users.uid.toString()
            )
            daftarkanPasien(reqPendaftaran)
        }
    }

    private fun daftarkanPasien(data: PendaftaranRequest) {
        CloudApi().daftarkan(data).enqueue(object: Callback<Pendaftaran> {
            override fun onResponse(call: Call<Pendaftaran>, response: Response<Pendaftaran>) {
                if(response.body()?.success == true) {
                    Toast.makeText(this@Pendaftaran, "Pendaftaran BERHASIL", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Pendaftaran, "Pendaftaran GAGAL", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<Pendaftaran>, t: Throwable) {}
        })

    }


    private fun updateDatePicker() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val times = sdf.format(call.time).toString()
        txt_input_tanggal.setText(times)
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