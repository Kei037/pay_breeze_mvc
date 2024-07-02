package com.kei037.pay_breeze_mvc.data

import android.app.Application
import android.util.Log
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.groupEventsByDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 어플 실행시 DB 초기화
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        val categoryList = mutableListOf<String>(
            "월급", "식비", "교통비", "커피/디저트", "쇼핑", "생활비", "통신비",
            "편의점", "공과금/관리비", "병원비", "반려동물", "공과금/관리비")

        CoroutineScope(Dispatchers.IO).launch {
            val categoryDao = db.getCategoryDao()
            val categoryCount = categoryDao.getAll().size

            if (categoryCount == 0) {
                for (i in 0..<categoryList.size) {
                    categoryDao.insertCategory(CategoryEntity(null, categoryList[i], true))
                }
            }
            // 테스트코드
            val testCode = categoryDao.getAll()
            for (i in testCode.indices) {
                Log.i("들어간 카테고리 이름 === ", testCode[i].toString())
            }
        }
    }
}
