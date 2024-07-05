package com.kei037.pay_breeze_mvc.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.databinding.ActivityHistoricalTransBinding

class HistoricalTransActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoricalTransBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricalTransBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }
    }
}