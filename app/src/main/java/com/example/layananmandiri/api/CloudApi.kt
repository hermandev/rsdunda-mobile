package com.example.layananmandiri.api

import com.example.layananmandiri.models.pasien.Pasien
import com.example.layananmandiri.models.users.Users
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface CloudApi {

    @GET("pendaftaran/norm/{norm}")
    fun getPasien(@Path("norm") norm:Int): Call<Pasien>

    @GET("users/:uid")
    fun checkUsers(@Path("uid") uid:String): Call<Users>

    companion object {
        val BASE_URL = "http://34.72.125.129/api/v1/"
        val BASE_URL_DEV = "http://192.168.1.6:5000/api/v1/"
        operator fun invoke(): CloudApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_DEV)
                .build()
                .create(CloudApi::class.java)
        }
    }
}