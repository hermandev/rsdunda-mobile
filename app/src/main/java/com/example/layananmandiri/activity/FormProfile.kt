package com.example.layananmandiri.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isNotEmpty
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.layananmandiri.MainActivity
import com.example.layananmandiri.R
import com.example.layananmandiri.api.CloudApi
import com.example.layananmandiri.models.users.UserRequest
import com.example.layananmandiri.models.users.Users
import com.example.layananmandiri.util.SharedUsers
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class FormProfile : AppCompatActivity() {
    lateinit var foto_profil:ImageView
    lateinit var imageUri:Uri
    lateinit var nama_lengkap:EditText
    lateinit var nik_ktp:EditText
    lateinit var no_hp:EditText
    lateinit var alamat:EditText
    lateinit var btnSimpan:MaterialButton
    lateinit var kelamin:RadioGroup
    lateinit var rKelamin:RadioButton
    lateinit var jenis_kelamin:String
    lateinit var storage:FirebaseStorage
    lateinit var storageRef:StorageReference
    lateinit var users: SharedUsers


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_profile)
        supportActionBar?.hide()

        // firebase init
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        users = SharedUsers(this@FormProfile)

        foto_profil = findViewById(R.id.foto_profil)
        nama_lengkap = findViewById(R.id.nama_lengkap)
        nik_ktp = findViewById(R.id.nik_ktp)
        no_hp = findViewById(R.id.no_hp)
        alamat = findViewById(R.id.alamat)
        kelamin = findViewById(R.id.kelamin)
        btnSimpan = findViewById(R.id.btnSimpan)

        kelamin.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when(checkedId) {
                    R.id.laki_laki ->
                        jenis_kelamin = "1"
                    R.id.perempuan ->
                        jenis_kelamin = "2"
                }
            }
        })
        // button onclick
        foto_profil.setOnClickListener {
            choosePicture()
        }

        btnSimpan.setOnClickListener {
            if(kelamin.checkedRadioButtonId != null) {
                if(jenis_kelamin == "1") {
                    Toast.makeText(this@FormProfile, "Kelamin : Laki - Laki", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this@FormProfile, "Kelamin : Perempuan", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this@FormProfile, "Kelamin Belum di pilih", Toast.LENGTH_SHORT).show()
            }
            prosesSimpan()
        }

        if(users.foto != "") {
            val ref = storageRef.child(users.foto.toString())
            ref.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
                override fun onSuccess(p0: Uri?) {
                    foto_profil.load(p0) {
                        crossfade(true)
                        transformations(CircleCropTransformation())
                    }
                }
            })
        }

    }

    private fun choosePicture() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
                imageUri = data?.data!!
                foto_profil.load(imageUri) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                    uploadFoto()
                }

        }
    }


    private fun prosesSimpan() {
        val userReq = UserRequest(
            nik = nik_ktp.text.toString(),
            fullname = nama_lengkap.text.toString(),
            kelamin= jenis_kelamin,
            foto = users.foto,
            nohp = no_hp.text.toString(),
            alamat = alamat.text.toString(),
            device = null,
            imei = null,
            session = users.uid.toString(),
        )

        CloudApi().simpanProfil(userReq).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if(response.body()?.success == true) {
                    users.fullname = response.body()?.data?.fullname
                    users.kelamin = response.body()?.data?.kelamin
                    users.alamat = response.body()?.data?.alamat
                    users.nik = response.body()?.data?.nik
                    users.profilCheck = true
                    startActivity(Intent(this@FormProfile, MainActivity::class.java))
                    finish()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {}
        })
    }

    private fun uploadFoto() {
        val dialog = ProgressDialog(this@FormProfile)
        dialog.setTitle("Upload Foto...")
        dialog.show()

        val randomKey = UUID.randomUUID().toString()
        val photoRef = storageRef.child("PROFIL/$randomKey")
        photoRef.putFile(imageUri)
            .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                    dialog.dismiss()
                    users.foto = randomKey
                    Snackbar.make(findViewById(android.R.id.content), "Upload success", Snackbar.LENGTH_LONG).show()
                }
            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Toast.makeText(this@FormProfile, "$p0", Toast.LENGTH_SHORT).show()
                }
            })
            .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot> {
                override fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                    val progress = (100.00 * snapshot.bytesTransferred / snapshot.totalByteCount)
                    dialog.setMessage("Percentage: $progress %")
                }
            })

    }
}