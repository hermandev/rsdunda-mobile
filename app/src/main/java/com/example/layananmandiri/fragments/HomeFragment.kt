package com.example.layananmandiri.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.layananmandiri.R
import com.example.layananmandiri.activity.Pendaftaran
import com.example.layananmandiri.api.CloudApi
import com.example.layananmandiri.models.pasien.Pasien
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt


class HomeFragment : Fragment() {
    private  val ARG_PARAM1 = "param1"
    private  val ARG_PARAM2 = "param2"
    private var param1: String? = null
    private var param2: String? = null

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
        val home_appbar = view.findViewById<AppBarLayout>(R.id.home_appbar)
        val home_scroll = view.findViewById<ScrollView>(R.id.home_scroll)
        home_appbar.bringToFront()

        home_scroll.setOnScrollChangeListener(View.OnScrollChangeListener { _i, scrollX, scrollY, oldScrollX, oldScrollY ->
            println("SCROLL ==> ${scrollX} AND ${scrollY} OLD ${oldScrollX} AND ${oldScrollY}")
            if(scrollY >= 60) {
                home_appbar.setBackgroundColor(resources.getColor(R.color.main_color))
            } else {
                home_appbar.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }
        })
        println("SCROLL ==> ${home_scroll.height}")


        val btn_pendaftaran = view.findViewById<MaterialCardView>(R.id.btn_pendaftaran)



        btn_pendaftaran.setOnClickListener {
            popupDaftar()
        }

//        getPasien(1)

    }

    private fun getPasien(norm:Int) {
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
                startActivity(intent)
            }

            override fun onFailure(call: Call<Pasien>, t: Throwable) {

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
            GlobalScope.launch(Dispatchers.IO) {
                getPasien(parseInt(norm.text.toString()))
            }
            mAlertDialog?.dismiss()
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

}


