package com.kei037.pay_breeze_mvc.ui.setting

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.databinding.ActivityCustomCategoryBinding
import com.kei037.pay_breeze_mvc.ui.setting.settingAdapter.CustomCategoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomCategoryBinding // 뷰 바인딩 객체 선언
    private var db: AppDatabase? = null // 데이터베이스 객체 선언
    private lateinit var adapter: CustomCategoryAdapter // 어댑터 객체 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomCategoryBinding.inflate(layoutInflater) // 뷰 바인딩 초기화
        setContentView(binding.root) // 뷰 설정
        // 액션바 숨기기
        supportActionBar?.hide()

        db = AppDatabase.getInstance(this) // 데이터베이스 인스턴스 가져오기

        binding.backBtn.setOnClickListener { finish() } // 뒤로 가기 버튼 클릭 리스너 설정

        setupRecyclerView() // 리사이클러뷰 설정
        loadCategories() // 카테고리 로드
    }

    private fun setupRecyclerView() {
        adapter = CustomCategoryAdapter(
            onCategoryDeleted = { category -> deleteCategory(category) }, // 카테고리 삭제 콜백 설정
            onAddCategory = { showAddCategoryDialog() } // 카테고리 추가 콜백 설정
        )

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2) // 2열 그리드 레이아웃 설정
        binding.recyclerView.adapter = adapter // 어댑터 설정
    }

    private fun loadCategories() {
        lifecycleScope.launch(Dispatchers.IO) {
            val categories = db?.getCategoryDao()?.getCategoriesByPublicStatus(false) // 비공개 카테고리 가져오기
            categories?.let {
                launch(Dispatchers.Main) {
                    adapter.submitList(it) // 메인 스레드에서 어댑터에 카테고리 리스트 설정
                }
            }
        }
    }

    private fun deleteCategory(category: CategoryEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            db?.getCategoryDao()?.deleteCategory(category) // 카테고리 삭제
            loadCategories() // 리스트를 새로고침
        }
    }

    private fun showAddCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null) // 다이얼로그 뷰 인플레이트
        val dialog = AlertDialog.Builder(this)
            .setTitle("카테고리 추가") // 다이얼로그 제목 설정
            .setView(dialogView) // 다이얼로그에 뷰 설정
            .setPositiveButton("추가") { _, _ ->
                val categoryName = dialogView.findViewById<EditText>(R.id.categoryNameInput).text.toString() // 입력된 카테고리 이름 가져오기
                if (categoryName.isNotBlank()) {
                    if (isCheck(categoryName)) {
                        addCategory(categoryName)
                    }
                    else {
                        Toast.makeText(applicationContext, "중복된 카테고리가 존재합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("취소", null) // 취소 버튼 설정
            .create()
        dialog.show() // 다이얼로그 표시
    }

    private fun addCategory(categoryName: String) {
        val categoryDao = db!!.getCategoryDao() // 카테고리 DAO 가져오기
        val newCategory = CategoryEntity(name = categoryName, isPublic = false) // 새로운 카테고리 엔티티 생성

        lifecycleScope.launch(Dispatchers.IO) {
            categoryDao.insertCategory(newCategory) // 새로운 카테고리 삽입
            launch(Dispatchers.Main) {
                loadCategories() // 새로운 카테고리를 추가한 후 리스트를 새로고침
            }
        }
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

}
