package com.example.layananmandiri.api

import com.example.layananmandiri.models.pasien.Pasien
import com.example.layananmandiri.models.pendaftaran.Pendaftaran
import com.example.layananmandiri.models.pendaftaran.PendaftaranRequest
import com.example.layananmandiri.models.poli.Poli
import com.example.layananmandiri.models.users.UserRequest
import com.example.layananmandiri.models.users.Users
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface CloudApi {

    @GET("pendaftaran/norm/{norm}")
    fun getPasien(@Path("norm") norm:Int): Call<Pasien>

    @GET("poliklinik")
    fun getPoli(): Call<Poli>

    @GET("users/{uid}")
    fun checkUsers(@Path("uid") uid:String): Call<Users>

    @POST("users")
    fun simpanProfil(@Body req: UserRequest): Call<Users>

    @POST("pendaftaran")
    fun daftarkan(@Body req: PendaftaranRequest): Call<Pendaftaran>


    companion object {
        val BASE_URL = "http://34.122.179.113/api/v1/"
        val BASE_URL_DEV = "http://192.168.5.46:5000/api/v1/"
        operator fun invoke(): CloudApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(CloudApi::class.java)
        }
    }
}