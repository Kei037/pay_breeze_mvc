package com.kei037.pay_breeze_mvc.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Query("SELECT * FROM TransactionEntity ORDER BY transaction_date DESC")
    fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM TransactionEntity WHERE transaction_date = :date ORDER BY transaction_date DESC")
    fun getTransactionsByDate(date: String): List<TransactionEntity>

    @Query("SELECT * FROM TransactionEntity WHERE transaction_date BETWEEN :startDate AND :endDate ORDER BY transaction_date DESC")
    fun getTransactionsByDateRange(startDate: String, endDate: String): List<TransactionEntity>

    @Query("SELECT * FROM TransactionEntity WHERE id = :id")
    fun getTransactionsByID(id: Long): TransactionEntity

    @Query("SELECT SUM(amount) FROM TransactionEntity WHERE amount > 0 AND transaction_date = :date")
    fun getTotalIncomeByDate(date: String): Double?

    @Query("SELECT SUM(amount) FROM TransactionEntity WHERE amount < 0 AND transaction_date = :date")
    fun getTotalExpenseByDate(date: String): Double?

    @Insert
    fun insertTransaction(transaction: TransactionEntity)

    @Delete
    fun deleteTransaction(transaction: TransactionEntity)

    @Update
    fun updateTransaction(transaction: TransactionEntity)

}