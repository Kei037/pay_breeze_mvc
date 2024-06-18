package com.kei037.pay_breeze_mvc.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kei037.pay_breeze_mvc.data.db.converter.Converters
import com.kei037.pay_breeze_mvc.data.db.dao.CategoryDao
import com.kei037.pay_breeze_mvc.data.db.dao.TransactionDao
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity

@TypeConverters(Converters::class)
@Database(entities = arrayOf(TransactionEntity::class, CategoryEntity::class), version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun getTransactionDao(): TransactionDao
    abstract fun getCategoryDao(): CategoryDao

    companion object {
        val dataBaseName = "pay_breeze_db"
        private var appDatabase : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context,
                    AppDatabase::class.java, dataBaseName).build()
            }
            return appDatabase
        }
    }
}