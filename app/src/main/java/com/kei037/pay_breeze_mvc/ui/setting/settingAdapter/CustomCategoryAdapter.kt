package com.kei037.pay_breeze_mvc.ui.setting.settingAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.databinding.ItemCategoryBinding

class CustomCategoryAdapter(
    private val onCategoryDeleted: (CategoryEntity) -> Unit, // 카테고리 삭제 콜백
    private val onAddCategory: () -> Unit // 카테고리 추가 콜백
) : ListAdapter<CategoryEntity, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemCount(): Int {
        return super.getItemCount() + 1 // "카테고리 추가" 항목을 포함하기 위해 +1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) { // 마지막 항목 위치일 때
            R.layout.item_add_category // "카테고리 추가" 항목의 레이아웃 리소스 ID 반환
        } else {
            R.layout.item_category // 일반 카테고리 항목의 레이아웃 리소스 ID 반환
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_add_category) {
            // "카테고리 추가" 항목의 ViewHolder 생성
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_category, parent, false)
            AddCategoryViewHolder(view)
        } else {
            // 일반 카테고리 항목의 ViewHolder 생성
            val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CategoryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            // 일반 카테고리 항목을 ViewHolder에 바인딩
            val category = getItem(position)
            holder.bind(category)

            // 삭제 버튼 클릭 리스너 설정
            holder.binding.deleteBtn.setOnClickListener {
                Log.d("CategoryAdapter", "Delete button clicked for category: ${category.name}")
                onCategoryDeleted(category) // 카테고리 삭제 콜백 호출
            }
        } else if (holder is AddCategoryViewHolder) {
            // "카테고리 추가" 항목을 클릭했을 때 콜백 호출
            holder.itemView.setOnClickListener {
                onAddCategory() // 카테고리 추가 콜백 호출
            }
        }
    }

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        // 일반 카테고리 항목을 ViewHolder에 바인딩하는 메서드
        fun bind(category: CategoryEntity) {
            binding.categoryName.text = category.name
            binding.deleteBtn.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
    }

    class AddCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // "카테고리 추가" 항목의 ViewHolder
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryEntity>() {
        // 두 항목이 동일한 항목인지 확인하는 메서드
        override fun areItemsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        // 두 항목의 내용이 동일한지 확인하는 메서드
        override fun areContentsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}
