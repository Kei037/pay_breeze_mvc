package com.kei037.pay_breeze_mvc.ui.commons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.databinding.ActivityEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    private var id: String? = null
    private var title: String? = null
    private var amount: String? = null
    private var transactionDate: String? = null
    private var description: String? = null
    private var categoryName: String? = null

    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("id")
            title = bundle.getString("title")
            amount = bundle.getString("amount")
            transactionDate = bundle.getString("transactionDate")
            description = bundle.getString("description")
            categoryName = bundle.getString("categoryName")

            binding.editTitle.setText(title)
            binding.editAmount.setText(amount)
            binding.editDate.setText(transactionDate)
            binding.editDescription.setText(description)
            binding.editCategory.setText(categoryName)
        }
        binding.finishBtn.setOnClickListener {
            val editTitle = binding.editTitle.text.toString()
            val editAmount = binding.editAmount.text.toString().toDoubleOrNull() ?: 0.0
            val editDate = binding.editDate.text.toString()
            val editDescription = binding.editDescription.text.toString()
            val editCategory = binding.editCategory.text.toString()

            db = AppDatabase.getInstance(this)!!
            val transDao = db!!.getTransactionDao()

            lifecycleScope.launch(Dispatchers.IO) {
                val updatedTransaction = TransactionEntity(id?.toLong(), editTitle, editAmount, editDate, editDescription, editCategory)
                transDao.updateTransaction(updatedTransaction)
                val editTrans = id?.let { it1 -> transDao.getTransactionsByID(it1.toLong()) }
                Log.i("수정된 가계부 === ", editTrans.toString())

                val resultIntent = Intent().apply {
                    putExtra("updatedTransaction", editTrans.toString())
                }
                setResult(RESULT_OK, resultIntent)
                launch(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}