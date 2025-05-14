package com.example.e_ticaretuygulamasi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_ticaretuygulamasi.data.CartDao
import com.example.e_ticaretuygulamasi.data.CartItem
import com.example.e_ticaretuygulamasi.data.Food
import com.example.e_ticaretuygulamasi.data.FoodApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FoodViewModel(
    private val api: FoodApi,
    private val dao: CartDao
): ViewModel() {

    private val _foods = MutableLiveData<List<Food>>()
    val foods: LiveData<List<Food>> get() = _foods

    val cartItems: LiveData<List<CartItem>> = dao.getAll()

    private val _itemAddedToCart = MutableSharedFlow<CartItem>()
    val itemAddedToCart = _itemAddedToCart.asSharedFlow()

    private val _itemRemovedFromCart = MutableSharedFlow<CartItem>()
    val itemRemovedFromCart = _itemRemovedFromCart.asSharedFlow()

    fun loadFoods(){
        viewModelScope.launch {
            val response = api.getAllFoods()
            if(response.isSuccessful){
                _foods.postValue(response.body()?.yemekler ?: listOf())
            }
        }
    }

    fun addToCart(food: Food) {
        viewModelScope.launch {
            val cartItem = CartItem(name = food.yemekAdi, price = food.yemekFiyat, imageName = food.yemekResimAdi)
            dao.insert(cartItem)
            _itemAddedToCart.emit(cartItem) // Sepete eklenen öğeyi bildir
        }
    }

    fun deleteFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            dao.delete(cartItem)
            _itemRemovedFromCart.emit(cartItem) // Sepetten silinen öğeyi bildir
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            dao.clearCart()
        }
    }
}