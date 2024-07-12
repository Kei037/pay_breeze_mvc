package com.kei037.pay_breeze_mvc.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity

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

    @Query("SELECT * FROM CategoryEntity WHERE is_public = :isPublic")
    fun getCategoriesByPublicStatus(isPublic: Boolean): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity WHERE name = :name")
    fun getOneCategoryByName(name: String?): CategoryEntity?
}