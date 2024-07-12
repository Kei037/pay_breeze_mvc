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
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.databinding.ActivityEditCategoryBinding
import com.kei037.pay_breeze_mvc.ui.commons.commonsAdapter.CategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        // 액션바 숨기기
        supportActionBar?.hide()

        // 뒤로가기 버튼 설정
        binding.backBtn.setOnClickListener {
            finish()
        }

        // DB 초기화
        db = AppDatabase.getInstance(this)

        // Intent로부터 TransactionEntity 정보 가져오기
        transactionCategory = intent.extras?.getString("categoryName")

        // RecyclerView 설정
        setupRecyclerView()

        // 카테고리와 트랜잭션 카테고리를 불러와서 중복 검사 후 RecyclerView에 표시
        loadCategories()

        // Finish 버튼 클릭
        binding.finishBtn.setOnClickListener {
            onFinishButtonClicked()
        }
    }

    private fun setupRecyclerView() {
        adapter = CategoryAdapter(
            onCategorySelected = { category ->
                selectedCategory = category
            },
            onCategoryDeleted = { category ->
                deleteCategory(category)
            },
            onAddCategory = {
                showAddCategoryDialog()
            }
        )
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun loadCategories() {
        val categoryDao = db!!.getCategoryDao()

        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                categoryDao.getAll()
            }

            adapter.submitList(categories)
            transactionCategory?.let { adapter.setSelectedCategory(it) }
        }
    }

    private fun showAddCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("카테고리 추가")
            .setView(dialogView)
            .setPositiveButton("추가") { _, _ ->
                val categoryName = dialogView.findViewById<EditText>(R.id.categoryNameInput).text.toString()
                if (isCheck(categoryName)) {
                    addCategory(categoryName)
                }
                else {
                    Toast.makeText(applicationContext, "중복된 카테고리가 존재합니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소", null)
            .create()
        dialog.show()
    }

    private fun isCheck(categoryName: String): Boolean {
        var flag = false
        val categoryDao = db!!.getCategoryDao()

        lifecycleScope.launch(Dispatchers.IO) {
            if (categoryDao.getOneCategoryByName(categoryName) == null) {
                flag = true
            } else {
                flag = false
            }
        }
        return flag
    }

    private fun addCategory(categoryName: String) {
        val categoryDao = db!!.getCategoryDao()
        val newCategory = CategoryEntity(null, categoryName, false)

        lifecycleScope.launch(Dispatchers.IO) {
            categoryDao.insertCategory(newCategory)
            val categories = categoryDao.getAll()

            withContext(Dispatchers.Main) {
                adapter.submitList(categories)
                adapter.setSelectedCategory(categoryName)
                selectedCategory = newCategory
            }
        }
    }

    private fun deleteCategory(category: CategoryEntity) {
        if (category.isPublic) {
            Toast.makeText(this, "공개 카테고리는 삭제할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val categoryDao = db!!.getCategoryDao()
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("EditCategoryActivity", "Attempting to delete category: ${category.name} with id: ${category.id}")
            categoryDao.deleteCategory(category)
            val categories = categoryDao.getAll()

            withContext(Dispatchers.Main) {
                Log.d("EditCategoryActivity", "Updating UI with categories: $categories")
                adapter.submitList(categories)
                Toast.makeText(this@EditCategoryActivity, "${category.name} 카테고리가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onFinishButtonClicked() {
        selectedCategory?.let {
            val resultIntent = Intent().apply {
                putExtra("selectedCategory", it.name)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } ?: Toast.makeText(this, "카테고리를 선택하세요.", Toast.LENGTH_SHORT).show()
    }
}
