package com.kei037.pay_breeze_mvc.ui.commons

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.databinding.ActivityEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    // 가계부 저장값 임시 변수
    private var id: String? = null
    private var title: String? = null
    private var amount: String? = null
    private var transactionDate: String? = null
    private var description: String? = null
    private var categoryName: String? = null

    // DB 초기화
    private var db: AppDatabase? = null

    private var isExpense: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 액션바 숨기기
        supportActionBar?.hide()

        // 뒤로가기 버튼
        binding.backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // 상세화면에서 넘어온 값을 수정화면에 뿌려줌
        intent.extras?.let { bundle ->
            id = bundle.getString("id")
            title = bundle.getString("title")
            amount = bundle.getString("amount")
            transactionDate = bundle.getString("transactionDate")
            description = bundle.getString("description")
            categoryName = bundle.getString("categoryName")

            binding.editTitle.setText(title)
            binding.editAmount.setText(amount)
            binding.editDate.text = transactionDate
            binding.editDescription.setText(description)
            binding.editCategory.text = categoryName
        }

        // 언더라인 설정
        binding.finishBtn.paintFlags = binding.finishBtn.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // datePicker 생성
        binding.editDateLinear.setOnClickListener {
            showDatePicker()
        }

        // 카테고리 변경화면으로 Intent
        binding.editCategory.setOnClickListener {
            val intent = Intent(this, EditCategoryActivity::class.java).apply {
                putExtra("id", id)
                putExtra("categoryName", categoryName)
            }
            startActivityForResult(intent, REQUEST_CODE_CATEGORY)
        }

        // 수정된 값들을 저장
        binding.finishBtn.setOnClickListener {
            saveTransaction()
        }

        // 수입 / 지출 버튼 클릭시 색상 변경 및 음수 변경
        binding.chipIncome.setOnClickListener {
            selectChip(true)
        }

        binding.chipExpense.setOnClickListener {
            selectChip(false)
        }
    }

    // DataPicker 생성
    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("날짜 선택")
            .build()
        datePicker.show(supportFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener { selection ->
            // 선택된 날짜를 Long 형식으로 가져옴
            val selectedDateMillis = selection ?: return@addOnPositiveButtonClickListener

            // 날짜 형식을 원하는 포맷으로 변환
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(selectedDateMillis))

            // 변환된 날짜를 TextView에 설정
            binding.editDate.text = formattedDate
        }
    }

    // chip 선택시 색상변경
    private fun selectChip(isIncome: Boolean) {
        binding.chipIncome.apply {
            isChecked = isIncome
            setChipBackgroundColorResource(if (isIncome) R.color.customGreen else R.color.customIvory)
            setTextColor(if (isIncome) Color.WHITE else Color.BLACK)
        }

        binding.chipExpense.apply {
            isChecked = !isIncome
            setChipBackgroundColorResource(if (!isIncome) R.color.customGreen else R.color.customIvory)
            setTextColor(if (!isIncome) Color.WHITE else Color.BLACK)
        }

        isExpense = !isIncome
    }

    // 트랜잭션을 저장하는 함수
    private fun saveTransaction() {
        val editTitle = binding.editTitle.text.toString()
        val editAmountString = binding.editAmount.text.toString()
        var editAmount = editAmountString.replace(",", "").toDouble()

        // 수입/지출 여부에 따른 금액 처리
        if (isExpense == null) {
            editAmount = editAmount
        } else if (isExpense!! && editAmount > 0) {
            editAmount = -editAmount
        } else if (!isExpense!! && editAmount < 0) {
            editAmount = -editAmount
        }

        val editDate = binding.editDate.text.toString()
        val editDescription = binding.editDescription.text.toString()
        val editCategory = binding.editCategory.text.toString()

        // DB, DAO 가져옴
        db = AppDatabase.getInstance(this)
        val transDao = db!!.getTransactionDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val updatedTransaction = TransactionEntity(id?.toLong(), editTitle, editAmount, editDate, editDescription, editCategory)
            // 업데이트
            transDao.updateTransaction(updatedTransaction)
            // 업데이트된 가계부 가져옴
            val editTrans = id?.let { it1 -> transDao.getTransactionsByID(it1.toLong()) }
            Log.i("수정된 가계부 === ", editTrans.toString())
            // 업데이트된 가계부를 이전 화면에 전달
            val resultIntent = Intent().apply {
                putExtra("updatedTransaction", editTrans.toString())
            }
            withContext(Dispatchers.Main) {
                setResult(RESULT_OK, resultIntent)
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CATEGORY && resultCode == RESULT_OK) {
            val selectedCategory = data?.getStringExtra("selectedCategory")
            binding.editCategory.text = selectedCategory
            categoryName = selectedCategory
        }
    }

    companion object {
        private const val REQUEST_CODE_CATEGORY = 1
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
