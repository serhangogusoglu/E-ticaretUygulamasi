package com.example.e_ticaretuygulamasi.data

import retrofit2.Response
import retrofit2.http.GET

interface FoodApi {
    @GET("tumYemekleriGetir.php")
    suspend fun getAllFoods() : Response<FoodResponse>
}