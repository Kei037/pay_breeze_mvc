package com.kei037.pay_breeze_mvc.ui.commons

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.databinding.ActivityEditCategoryBinding
import com.kei037.pay_breeze_mvc.ui.commons.commonsAdapter.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBinding
    private lateinit var adapter: CategoryAdapter
    private var db: AppDatabase? = null
    private var selectedCategory: CategoryEntity? = null
    private var transactionCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        // DB 초기화
        db = AppDatabase.getInstance(this)

        // Intent로부터 TransactionEntity 정보 가져오기
        val bundle = intent.extras
        if (bundle != null) {
            transactionCategory = bundle.getString("categoryName")
        }

        // RecyclerView 설정
        adapter = CategoryAdapter(
            onCategorySelected = { category ->
                selectedCategory = category
            },
            onCategoryDeleted = { category ->
                deleteCategory(category)
            },
            onAddCategory = { // "카테고리 추가" 항목 클릭 시 콜백
                showAddCategoryDialog()
            }
        )
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2) // 2열 그리드
        binding.recyclerView.adapter = adapter

        // 카테고리와 트랜잭션 카테고리를 불러와서 중복 검사 후 RecyclerView에 표시
        loadCategories()

        // Finish 버튼 클릭
        binding.finishBtn.setOnClickListener {
            selectedCategory?.let {
                val resultIntent = Intent().apply {
                    putExtra("selectedCategory", it.name)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } ?: Toast.makeText(this, "카테고리를 선택하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCategories() {
        val categoryDao = db!!.getCategoryDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val categories = categoryDao.getAll()

            launch(Dispatchers.Main) {
                adapter.submitList(categories)
                transactionCategory?.let { adapter.setSelectedCategory(it) }
            }
        }
    }

    private fun showAddCategoryDialog() {
        // 카테고리를 추가하는 다이얼로그 표시
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("카테고리 추가")
            .setView(dialogView)
            .setPositiveButton("추가") { _, _ ->
                val categoryName = dialogView.findViewById<EditText>(R.id.categoryNameInput).text.toString()
                addCategory(categoryName)
            }
            .setNegativeButton("취소", null)
            .create()
        dialog.show()
    }

    private fun addCategory(categoryName: String) {
        val categoryDao = db!!.getCategoryDao()
        val newCategory = CategoryEntity(null, categoryName, false)

        lifecycleScope.launch(Dispatchers.IO) {
            categoryDao.insertCategory(newCategory)
            launch(Dispatchers.Main) {
                adapter.addCategory(newCategory) // 새로운 카테고리를 추가하고 선택 상태로 설정
                selectedCategory = newCategory // 새로 추가된 카테고리를 선택된 카테고리로 설정
            }
        }
    }

    private fun deleteCategory(category: CategoryEntity) {
        if (category.isPublic) {
            Toast.makeText(this@EditCategoryActivity, "공개 카테고리는 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val categoryDao = db!!.getCategoryDao()
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("EditCategoryActivity", "Attempting to delete category: ${category.name} with id: ${category.id}")
            categoryDao.deleteCategory(category)
            Log.d("EditCategoryActivity", "Category deleted from database: ${category.name} with id: ${category.id}")
            val categories = categoryDao.getAll()

            launch(Dispatchers.Main) {
                Log.d("EditCategoryActivity", "Updating UI with categories: $categories")
                adapter.submitList(categories)
                Toast.makeText(this@EditCategoryActivity, "${category.name} 카테고리가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
