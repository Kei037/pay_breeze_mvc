package com.kei037.pay_breeze_mvc.ui.commons

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.databinding.ActivityDetailedBinding

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달된 이벤트 세부 정보 받기
        val eventDetail = intent.getStringExtra("EVENT_DETAIL")
        Log.i("불러온 이벤트", eventDetail.toString())
    }
}