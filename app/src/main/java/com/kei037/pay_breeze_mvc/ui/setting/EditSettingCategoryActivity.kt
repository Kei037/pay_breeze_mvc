package com.kei037.pay_breeze_mvc.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kei037.pay_breeze_mvc.databinding.ActivityEditSettingCategoryBinding

class EditSettingCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSettingCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSettingCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }


    }
}