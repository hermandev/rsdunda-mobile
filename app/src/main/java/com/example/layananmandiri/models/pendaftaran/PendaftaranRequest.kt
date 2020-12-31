package com.example.layananmandiri.models.pendaftaran

data class PendaftaranRequest(
    var TGL_BOOKING:String,
    var NORM:String,
    var NIK:String,
    var TANGGAL_LAHIR:String,
    var POLI:String,
    var KEPESERTAAN:String,
    var KEPERLUAN:String,
    var JENIS_RUJUKAN:String,
    var NO_BPJS:String,
    var NO_RUJUKAN:String,
    var FOTO_KTP:String,
    var FOTO_RUJUKAN:String,
    var FOTO_KONTROL:String,
    var STATUS_BPJS:String,
    var NO_SEP:String,
    var STATUS_PELAYANAN:String,
    var STATUS:String,
    var session:String
)