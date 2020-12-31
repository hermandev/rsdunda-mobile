package com.example.layananmandiri.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.layananmandiri.R
import com.example.layananmandiri.activity.Pendaftaran
import com.example.layananmandiri.api.CloudApi
import com.example.layananmandiri.models.pasien.Pasien
import com.example.layananmandiri.models.users.Users
import com.example.layananmandiri.util.SharedUsers
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt


class HomeFragment : Fragment() {
    private  val ARG_PARAM1 = "param1"
    private  val ARG_PARAM2 = "param2"
    private var param1: String? = null
    private var param2: String? = null
    lateinit var textHeaderName:TextView
    lateinit var users: SharedUsers
    lateinit var foto_profil:ImageView
    lateinit var storage:FirebaseStorage
    lateinit var storageRef: StorageReference
    lateinit var username:TextView
    lateinit var btn_pendaftaran:MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // inisialisasi variabel
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        users = SharedUsers(this.requireContext())
        checkUserProfile(users.uid.toString())
        val home_appbar = view.findViewById<AppBarLayout>(R.id.home_appbar)
        val home_scroll = view.findViewById<ScrollView>(R.id.home_scroll)
        btn_pendaftaran = view.findViewById(R.id.btn_pendaftaran)
        textHeaderName = view.findViewById(R.id.textHeaderName)
        foto_profil = home_appbar.findViewById(R.id.foto_profil)
        username = home_appbar.findViewById(R.id.username)
        textHeaderName.text = ""
        // inisialisasi variabel END

        // event component
        home_appbar.bringToFront()

        checkFoto(users.foto.toString())
        username.text = users.fullname
        textHeaderName.text = users.fullname

        foto_profil.setOnClickListener {

        }

        home_scroll.setOnScrollChangeListener(View.OnScrollChangeListener { _i, scrollX, scrollY, oldScrollX, oldScrollY ->
            println("SCROLL ==> ${scrollX} AND ${scrollY} OLD ${oldScrollX} AND ${oldScrollY}")
            if(scrollY >= 60) {
                home_appbar.setBackgroundColor(resources.getColor(R.color.main_color))
            } else {
                home_appbar.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }
        })

        btn_pendaftaran.setOnClickListener {
            popupDaftar()
        }

//        getPasien(1)

//        if(!users.profilCheck) {
//            val intent = Intent(this.requireContext(), FormProfile::class.java)
//            startActivity(intent)
//            activity?.supportFragmentManager?.popBackStack()
//        }
        // event component END

    }


    private fun checkFoto(foto: String) {
        if(foto != "") {
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

    private fun getPasien(norm:Int) {
        val formDialog = LayoutInflater.from(context).inflate(R.layout.pop_up_loading, null)
        val dialogBuild = context?.let { it1 -> AlertDialog.Builder(it1) }
        dialogBuild?.setView(formDialog)
        val mAlertDialog = dialogBuild?.show()
        CloudApi().getPasien(norm).enqueue(object : Callback<Pasien> {
            override fun onResponse(call: Call<Pasien>, response: Response<Pasien>) {
                val intent = Intent(context, Pendaftaran::class.java)
                intent.putExtra("ID", response.body()?.data?.ID.toString())
                intent.putExtra("NORM", response.body()?.data?.NORM.toString())
                intent.putExtra("NAMA", response.body()?.data?.NAMA.toString())
                intent.putExtra("ALAMAT", response.body()?.data?.ALAMAT.toString())
                intent.putExtra("TANGGAL_LAHIR", response.body()?.data?.TANGGAL_LAHIR.toString())
                intent.putExtra("JENIS_KELAMIN", response.body()?.data?.JENIS_KELAMIN.toString())
                intent.putExtra("STATUS", response.body()?.data?.STATUS.toString())
                mAlertDialog?.dismiss()
                startActivity(intent)
            }

            override fun onFailure(call: Call<Pasien>, t: Throwable) {
                mAlertDialog?.dismiss()
                Toast.makeText(context, "Nomor Rekam Medik tidak di temukan", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun popupDaftar() {
        val formDialog = LayoutInflater.from(context).inflate(R.layout.pop_up_daftar, null)
        val dialogBuild = context?.let { it1 -> AlertDialog.Builder(it1) }
        dialogBuild?.setView(formDialog)
        val mAlertDialog = dialogBuild?.show()
        formDialog.findViewById<MaterialButton>(R.id.btn_popup_norm).setOnClickListener {
            popupNORM()
            mAlertDialog?.dismiss()
        }
        formDialog.findViewById<MaterialButton>(R.id.btn_popup_nik).setOnClickListener {
            popupNIK()
            mAlertDialog?.dismiss()
        }
    }

    private fun popupNORM() {
        val formDialog = LayoutInflater.from(context).inflate(R.layout.pop_up_nomr, null)
        val dialogBuild = context?.let { it1 -> AlertDialog.Builder(it1) }
        dialogBuild?.setView(formDialog)
        val mAlertDialog = dialogBuild?.show()
        formDialog.findViewById<MaterialButton>(R.id.btn_check_norm).setOnClickListener {
            var norm = formDialog.findViewById<EditText>(R.id.check_input_norm)
            mAlertDialog?.dismiss()
//            GlobalScope.launch(Dispatchers.IO) {
            getPasien(parseInt(norm.text.toString()))
//            }

        }
    }
    private fun popupNIK() {
        val formDialog = LayoutInflater.from(context).inflate(R.layout.pop_up_nik, null)
        val dialogBuild = context?.let { it1 -> AlertDialog.Builder(it1) }
        dialogBuild?.setView(formDialog)
        val mAlertDialog = dialogBuild?.show()
        formDialog.findViewById<MaterialButton>(R.id.btn_check_nik).setOnClickListener {
            mAlertDialog?.dismiss()
            val intent = Intent(this.context, Pendaftaran::class.java)
            startActivity(intent)
        }
    }

    private fun checkUserProfile(uid:String) {
        CloudApi().checkUsers(uid).enqueue(object :Callback<Users>{
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
                }
                users.profilCheck = true
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {}
        })
    }

}


