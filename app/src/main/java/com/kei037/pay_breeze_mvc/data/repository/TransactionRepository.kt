package com.kei037.pay_breeze_mvc.data.repository

import android.content.Context
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity

class TransactionRepository(context: Context) {
    private val transactionDao = AppDatabase.getInstance(context).getTransactionDao()

    fun getAllTransactions(): List<TransactionEntity> {
        return transactionDao.getAll()
    }

    fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    fun getTotalIncomeByDate(date: String): Double {
        return transactionDao.getTotalIncomeByDate(date) ?: 0.0
    }

    fun getTotalExpenseByDate(date: String): Double {
        return transactionDao.getTotalExpenseByDate(date) ?: 0.0
    }
}
