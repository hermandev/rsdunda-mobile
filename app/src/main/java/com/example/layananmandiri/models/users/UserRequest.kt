package com.example.layananmandiri.models.users


data class UserRequest(
    val nik: String,
    val fullname: String,
    val kelamin: String,
    val foto: String?,
    val nohp: String,
    val alamat: String,
    val device: String?,
    val imei: String?,
    val session: String,
)