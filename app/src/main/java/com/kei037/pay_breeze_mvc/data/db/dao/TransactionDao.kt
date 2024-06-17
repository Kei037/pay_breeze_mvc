package com.kei037.pay_breeze.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kei037.pay_breeze.data.db.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Query("SELECT * FROM TransactionEntity")
    fun getAll(): List<TransactionEntity>

    @Insert
    fun insertTransaction(transaction: TransactionEntity)

    @Delete
    fun deleteTransaction(transaction: TransactionEntity)
}