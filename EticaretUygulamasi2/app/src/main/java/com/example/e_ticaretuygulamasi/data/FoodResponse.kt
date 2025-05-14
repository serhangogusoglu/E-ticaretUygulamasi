package com.example.e_ticaretuygulamasi.data

import com.example.e_ticaretuygulamasi.data.Food
import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @SerializedName("yemekler")
    val yemekler: List<Food>
)
