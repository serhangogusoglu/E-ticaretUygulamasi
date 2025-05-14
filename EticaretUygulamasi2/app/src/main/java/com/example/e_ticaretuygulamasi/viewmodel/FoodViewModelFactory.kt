package com.example.e_ticaretuygulamasi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_ticaretuygulamasi.data.CartDao
import com.example.e_ticaretuygulamasi.data.FoodApi

class FoodViewModelFactory(
    private val api: FoodApi,
    private val dao: CartDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  FoodViewModel(api, dao) as T
    }
}