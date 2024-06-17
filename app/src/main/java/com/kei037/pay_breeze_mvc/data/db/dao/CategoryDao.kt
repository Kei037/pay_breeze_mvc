package com.kei037.pay_breeze.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kei037.pay_breeze.data.db.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntity")
    fun getAll(): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity WHERE name = :name")
    fun getCategoryByName(name: String?): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: CategoryEntity)

    @Delete
    fun deleteCategory(category: CategoryEntity)
}