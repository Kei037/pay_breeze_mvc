package com.kei037.pay_breeze_mvc.ui.addition

import android.content.Context
import android.util.Log
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity

/**
 * 트랜잭션 및 카테고리 데이터베이스 접근을 담당하는 리포지토리 클래스.
 * CRUD 작업을 위해 데이터베이스와 상호작용하는 메서드를 제공
 */
class AdditionRepository(context: Context) {
    // 데이터베이스 인스턴스
    private val db = AppDatabase.getInstance(context)
    // 트랜잭션에 접근하기 위한 DAO
    private val transactionDao = db.getTransactionDao()
    // 카테고리에 접근하기 위한 DAO
    private val categoryDao = db.getCategoryDao()

    /**
     * 데이터베이스에서 모든 트랜잭션을 가져옴
     * @return List<TransactionEntity> 모든 트랜잭션 목록.
     */
    fun getAllTransactions(): List<TransactionEntity> {
        return transactionDao.getAll()
    }

    /**
     * 새로운 트랜잭션을 데이터베이스에 삽입
     * @param transaction 삽입할 트랜잭션 엔티티.
     */
    fun insertTransaction(transaction: TransactionEntity) {
        try {
            transactionDao.insertTransaction(transaction)
            Log.d("AdditionRepository", "Inserted transaction: $transaction")
        } catch (e: Exception) {
            Log.e("AdditionRepository", "Error inserting transaction: $transaction", e)
        }
    }

    /**
     * 여러 트랜잭션을 데이터베이스에 삽입
     * @param transactions 삽입할 트랜잭션 목록.
     */
    fun insertAllTransactions(transactions: List<TransactionEntity>) {
        transactions.forEach {
            insertTransaction(it)
        }
    }

    /**
     * 트랜잭션을 데이터베이스에서 삭제
     * @param transaction 삭제할 트랜잭션 엔티티.
     */
    fun deleteTransaction(transaction: TransactionEntity) {
        try {
            transactionDao.deleteTransaction(transaction)
            Log.d("AdditionRepository", "Deleted transaction: $transaction")
        } catch (e: Exception) {
            Log.e("AdditionRepository", "Error deleting transaction: $transaction", e)
        }
    }

    /**
     * 데이터베이스에서 모든 카테고리를 가져옴
     * @return List<CategoryEntity> 모든 카테고리 목록.
     */
    fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAll()
    }

    /**
     * 새로운 카테고리를 데이터베이스에 삽입합니다.
     * @param category 삽입할 카테고리 엔티티.
     */
    fun insertCategory(category: CategoryEntity) {
        try {
            categoryDao.insertCategory(category)
            Log.d("AdditionRepository", "Inserted category: $category")
        } catch (e: Exception) {
            Log.e("AdditionRepository", "Error inserting category: $category", e)
        }
    }

    /**
     * 카테고리를 데이터베이스에서 삭제
     * @param category 삭제할 카테고리 엔티티.
     */
    fun deleteCategory(category: CategoryEntity) {
        try {
            categoryDao.deleteCategory(category)
            Log.d("AdditionRepository", "Deleted category: $category")
        } catch (e: Exception) {
            Log.d("AdditionRepository", "Error deleting category: $category", e)
        }
    }

    /**
     * 이름으로 카테고리를 검색
     * @param name 검색할 카테고리 이름.
     * @return List<CategoryEntity> 검색된 카테고리 목록.
     */
    fun getCategoryByName(name: String): List<CategoryEntity> {
        return categoryDao.getCategoryByName(name)
    }
}
