package com.example.e_ticaretuygulamasi.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    suspend fun insert(cartItem: CartItem)

    @Query("SELECT * FROM cart")
    fun getAll(): LiveData<List<CartItem>>

    @Delete
    suspend fun delete(cartItem: CartItem) // Silme metodu

    @Query("DELETE  FROM cart")
    suspend fun clearCart()
}