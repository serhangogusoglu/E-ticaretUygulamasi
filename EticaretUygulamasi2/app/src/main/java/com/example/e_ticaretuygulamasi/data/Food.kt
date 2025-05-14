package com.example.e_ticaretuygulamasi.data

import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("yemek_adi")
    val yemekAdi: String,
    @SerializedName("yemek_resim_adi")
    val yemekResimAdi: String,
    @SerializedName("yemek_fiyat")
    val yemekFiyat: Int
)