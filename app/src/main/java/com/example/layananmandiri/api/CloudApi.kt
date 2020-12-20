package com.example.layananmandiri.api

import com.example.layananmandiri.models.Pasien
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface CloudApi {

    @GET("pendaftaran/norm/1")
    fun getPasien(): Call<Pasien>

    companion object {
        operator fun invoke(): CloudApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://34.72.125.129/api/v1/")
                .build()
                .create(CloudApi::class.java)
        }
    }
}