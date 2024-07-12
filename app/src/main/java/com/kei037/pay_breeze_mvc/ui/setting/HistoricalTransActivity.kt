package com.kei037.pay_breeze_mvc.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.databinding.ActivityHistoricalTransBinding
import com.kei037.pay_breeze_mvc.ui.setting.settingAdapter.HistoricalTransAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoricalTransActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoricalTransBinding
    private var db: AppDatabase? = null
    private lateinit var adapter: HistoricalTransAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricalTransBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 액션바 숨기기
        supportActionBar?.hide()

        binding.backBtn.setOnClickListener { finish() }

        db = AppDatabase.getInstance(this) // 데이터베이스 인스턴스 초기화

        setupRecyclerView()
        loadFilteredTransactions()
    }

    private fun setupRecyclerView() {
        adapter = HistoricalTransAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun loadFilteredTransactions() {
        lifecycleScope.launch(Dispatchers.IO) {
            val categories = db?.getCategoryDao()?.getAll() ?: emptyList<CategoryEntity>()
            val categoryNames = categories.map { it.name }.toSet()

            val transactions = db?.getTransactionDao()?.getAll() ?: emptyList<TransactionEntity>()
            val filteredTransactions = transactions.filter { it.categoryName !in categoryNames }

            withContext(Dispatchers.Main) {
                adapter.submitList(filteredTransactions) // 어댑터에 필터링된 거래 내역 리스트 설정
            }
        }
    }
}
