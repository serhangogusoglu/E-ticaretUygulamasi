package com.example.e_ticaretuygulamasi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartItem::class], version = 1)
abstract class CartDatabase : RoomDatabase() {  // abstract olduğu için doğrudan örneği oluşturulmaz
    abstract fun cartDao(): CartDao

    companion object {  // tek bir örnek olarak tutulur.
        @Volatile // @Volatile ile farklı thread’lerde görünürlük sağlanır.
        private var INSTANCE: CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase {
            return INSTANCE ?: synchronized(this) {  // synchronized(this) ile çoklu thread'lerin aynı anda veri tabanı oluşturmaması sağlanır (güvenli singleton).
                val instance = Room.databaseBuilder(
                    context.applicationContext,  // Bellek sızıntısı (memory leak) olmaması için.
                    CartDatabase::class.java,
                    "cart.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
