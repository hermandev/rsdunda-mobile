package com.example.layananmandiri.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.layananmandiri.R
import com.example.layananmandiri.activity.Pendaftaran
import com.example.layananmandiri.api.CloudApi
import com.example.layananmandiri.models.Pasien
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn_pendaftaran = view.findViewById<MaterialCardView>(R.id.btn_pendaftaran)


        btn_pendaftaran.setOnClickListener {
            popupDaftar()
//            val intent = Intent(this.context, Pendaftaran::class.java)
//            startActivity(intent)
        }

//        getPasien()

    }

    private fun getPasien() {
        CloudApi().getPasien().enqueue(object : Callback<Pasien> {
            override fun onResponse(call: Call<Pasien>, response: Response<Pasien>) {
                TODO("Not yet implemented")
                if(response.isSuccessful) {
                    println(response.body()?.data)
                } else {
                    println("Error")
                }
            }

            override fun onFailure(call: Call<Pasien>, t: Throwable) {
                TODO("Not yet implemented")
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
            mAlertDialog?.dismiss()
            val intent = Intent(this.context, Pendaftaran::class.java)
            startActivity(intent)
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