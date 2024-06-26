package com.kei037.pay_breeze_mvc.ui.commons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kei037.pay_breeze_mvc.databinding.ActivityDetailedBinding
import android.graphics.Paint
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.kei037.pay_breeze_mvc.data.db.AppDatabase

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
                    // 수정된 트랜잭션 문자열을 파싱하여 각 필드 추출
                    val cleanedString = it.removePrefix("TransactionEntity(").removeSuffix(")")
                    val parts = cleanedString.split(", ").associate {
                        val (key, value) = it.split("=")
                        key to value
                    }

                    // 파싱된 데이터를 UI에 반영
                    val title = parts["title"]
                    val amount = parts["amount"]
                    val transactionDate = parts["transactionDate"]
                    val description = parts["description"]
                    val categoryName = parts["categoryName"]

                    binding.titleText.text = title
                    binding.amountText.text = amount
                    binding.dateText.text = transactionDate
                    binding.descriptionText.text = description
                    binding.categoryText.text = categoryName
                }
            }
        }

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            finish()
        }

        // 전달된 이벤트 세부 정보 받기
        val transactionEntityString = intent.getStringExtra("EVENT_DETAIL")
        Log.i("불러온 이벤트", transactionEntityString.toString())

        // 전달된 문자열을 클리닝하고 파싱하여 각 필드 추출
        val cleanedString = transactionEntityString?.removePrefix("TransactionEntity(")?.removeSuffix(")")
        val parts = cleanedString?.split(", ")?.associate {
            val (key, value) = it.split("=")
            key to value
        }

        // 파싱된 데이터를 각 변수에 할당
        val id = parts?.get("id")
        val title = parts?.get("title")
        val amount = parts?.get("amount")
        val transactionDate = parts?.get("transactionDate")
        val description = parts?.get("description")
        val categoryName = parts?.get("categoryName")

        // UI에 초기 데이터 설정
        binding.titleText.text = title
        binding.amountText.text = amount
        binding.dateText.text = transactionDate
        binding.descriptionText.text = description
        binding.categoryText.text = categoryName

        // editBtn에 밑줄 설정 및 클릭 리스너 추가
        val editBtn = binding.editBtn
        editBtn.paintFlags = editBtn.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        editBtn.setOnClickListener {
            // EditActivity로 이동하는 Intent 생성
            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra("id", id)
                putExtra("title", title)
                putExtra("amount", amount)
                putExtra("transactionDate", transactionDate)
                putExtra("description", description)
                putExtra("categoryName", categoryName)
            }
            // EditActivity 시작
            editActivityResultLauncher.launch(intent)
        }
    }
}