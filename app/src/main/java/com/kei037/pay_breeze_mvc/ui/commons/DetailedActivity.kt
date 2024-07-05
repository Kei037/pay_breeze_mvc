package com.kei037.pay_breeze_mvc.ui.commons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kei037.pay_breeze_mvc.databinding.ActivityDetailedBinding
import android.graphics.Paint
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding
    private lateinit var db: AppDatabase
    private lateinit var editActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터베이스 인스턴스 초기화
        db = AppDatabase.getInstance(this)

        // ActivityResultLauncher 초기화
        editActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // EditActivity에서 업데이트된 결과를 처리
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val updatedTransactionString = data?.getStringExtra("updatedTransaction")
                updatedTransactionString?.let {
                    updateUIWithTransactionString(it)
                }
            }
        }

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // 전달된 이벤트 세부 정보 받기
        val transactionEntityString = intent.getStringExtra("EVENT_DETAIL")
        Log.i("불러온 이벤트", transactionEntityString.toString())

        // 전달된 문자열을 클리닝하고 파싱하여 각 필드 추출
        transactionEntityString?.let {
            updateUIWithTransactionString(it)
        }

        // editBtn에 밑줄 설정 및 클릭 리스너 추가
        val editBtn = binding.editBtn
        editBtn.paintFlags = editBtn.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        editBtn.setOnClickListener {
            // EditActivity로 이동하는 Intent 생성
            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra("id", binding.titleText.tag as? String)
                putExtra("title", binding.titleText.text.toString())
                putExtra("amount", binding.amountText.text.toString())
                putExtra("transactionDate", binding.dateText.text.toString())
                putExtra("description", binding.descriptionText.text.toString())
                putExtra("categoryName", binding.categoryText.text.toString())
            }
            // EditActivity 시작
            editActivityResultLauncher.launch(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.deleteBtn.setOnClickListener {

        }
    }


    private fun updateUIWithTransactionString(transactionEntityString: String) {
        // 전달된 문자열을 클리닝하고 파싱하여 각 필드 추출
        val cleanedString = transactionEntityString.removePrefix("TransactionEntity(").removeSuffix(")")
        val parts = cleanedString.split(", ").associate {
            val (key, value) = it.split("=")
            key to value
        }

        // 파싱된 데이터를 각 변수에 할당
        val id = parts["id"]
        val title = parts["title"]
        val amount = parts["amount"]
        val transactionDate = parts["transactionDate"]
        val description = parts["description"]
        val categoryName = parts["categoryName"]

        // UI에 초기 데이터 설정
        binding.titleText.tag = id
        binding.titleText.text = title
        binding.amountText.text = amount
        binding.dateText.text = transactionDate
        binding.descriptionText.text = description
        binding.categoryText.text = categoryName
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}