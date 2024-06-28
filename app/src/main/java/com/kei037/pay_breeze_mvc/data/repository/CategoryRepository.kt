package com.kei037.pay_breeze_mvc.data.repository

import android.content.Context
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity

class CategoryRepository(context: Context) {
    private val categoryDao = AppDatabase.getInstance(context).getCategoryDao()

    fun getAllCategories(): List<CategoryEntity> {
        return categoryDao.getAll()
    }

    fun insertCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    fun deleteCategory(category: CategoryEntity) {
        categoryDao.deleteCategory(category)
    }

    fun getCategoryByName(name: String): List<CategoryEntity> {
        return categoryDao.getCategoryByName(name)
    }
}