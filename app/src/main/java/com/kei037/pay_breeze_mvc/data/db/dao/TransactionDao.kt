package com.kei037.pay_breeze_mvc.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Query("SELECT * FROM TransactionEntity")
    fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM TransactionEntity WHERE transaction_date = :date")
    fun getTransactionsByDate(date: String): List<TransactionEntity>

    @Query("SELECT * FROM TransactionEntity WHERE transaction_date BETWEEN :startDate AND :endDate")
    fun getTransactionsByDateRange(startDate: String, endDate: String): List<TransactionEntity>

    @Insert
    fun insertTransaction(transaction: TransactionEntity)

    @Delete
    fun deleteTransaction(transaction: TransactionEntity)

}