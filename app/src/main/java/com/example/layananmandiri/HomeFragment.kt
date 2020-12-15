package com.example.layananmandiri

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.layananmandiri.activity.Pendaftaran
import com.google.android.material.card.MaterialCardView


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
            val intent = Intent(this.context, Pendaftaran::class.java)
            startActivity(intent)
        }
    }
}