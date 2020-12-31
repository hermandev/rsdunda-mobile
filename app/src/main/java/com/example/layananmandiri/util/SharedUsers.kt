package com.example.layananmandiri.util

import android.content.Context
import android.preference.PreferenceManager

class SharedUsers(val context: Context) {
    companion object {
        private const val UID = "UID"
        private const val NOHP = "NOHP"
        private const val NORM = "NORM"
        private const val NIK = "NIK"
        private const val FULLNAME = "FULLNAME"
        private const val KELAMIN = "KELAMIN"
        private const val FOTO = "FOTO"
        private const val ALAMAT = "ALAMAT"
        private const val AUTH = "AUTH"
        private const val CHECK = "CHECK"
    }

    private val data = PreferenceManager.getDefaultSharedPreferences(context)

    var uid = data.getString(UID, "")
    set(value) = data.edit().putString(UID, value).apply()

    var nomorhp = data.getString(NOHP, "")
    set(value) = data.edit().putString(NOHP, value).apply()

    var norm = data.getString(NORM, "")
        set(value) = data.edit().putString(NORM, value).apply()

    var nik = data.getString(NIK, "")
        set(value) = data.edit().putString(NIK, value).apply()

    var fullname = data.getString(FULLNAME, "")
        set(value) = data.edit().putString(FULLNAME, value).apply()

    var kelamin = data.getString(KELAMIN, "")
        set(value) = data.edit().putString(KELAMIN, value).apply()

    var foto = data.getString(FOTO, "")
        set(value) = data.edit().putString(FOTO, value).apply()

    var alamat = data.getString(ALAMAT, "")
        set(value) = data.edit().putString(ALAMAT, value).apply()

    var isAuth = data.getBoolean(AUTH, false)
    set(value) = data.edit().putBoolean(AUTH, value).apply()

    var profilCheck = data.getBoolean(CHECK, false)
        set(value) = data.edit().putBoolean(CHECK, value).apply()


}