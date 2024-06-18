package com.kei037.pay_breeze_mvc.data

import android.app.Application
import com.kei037.pay_breeze_mvc.data.db.AppDatabase

/**
 * 어플 실행시 DB 초기화
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}