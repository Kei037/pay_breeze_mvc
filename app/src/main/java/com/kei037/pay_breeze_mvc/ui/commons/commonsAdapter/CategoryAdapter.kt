package com.kei037.pay_breeze_mvc.ui.commons.commonsAdapter

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

// 카테고리 선택 콜백을 받는 어댑터 클래스 정의
class CategoryAdapter(
    private val onCategorySelected: (CategoryEntity) -> Unit,
    private val onCategoryDeleted: (CategoryEntity) -> Unit,
    private val onAddCategory: () -> Unit // 카테고리 추가 콜백 추가
) : ListAdapter<CategoryEntity, RecyclerView.ViewHolder>(DiffCallback()) {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun getItemCount(): Int {
        return super.getItemCount() + 1 // "카테고리 추가" 항목을 포함하기 위해 +1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            R.layout.item_add_category // "카테고리 추가" 항목의 레이아웃
        } else {
            R.layout.item_category // 일반 카테고리 항목의 레이아웃
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_add_category) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_category, parent, false)
            AddCategoryViewHolder(view)
        } else {
            val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CategoryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val category = getItem(position)
            holder.bind(category, selectedPosition == position)

            holder.itemView.setOnClickListener {
                val previousSelectedPosition = selectedPosition
                selectedPosition = holder.bindingAdapterPosition

                // 이전 선택된 항목 갱신
                if (previousSelectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelectedPosition)
                }
                // 현재 선택된 항목 갱신
                notifyItemChanged(selectedPosition)

                onCategorySelected(category)
            }

            holder.binding.deleteBtn.apply {
                visibility = if (category.isPublic) View.GONE else View.VISIBLE
                setOnClickListener {
                    Log.d("CategoryAdapter", "Delete button clicked for category: ${category.name}")
                    onCategoryDeleted(category)
                }
            }
        } else if (holder is AddCategoryViewHolder) {
            holder.itemView.setOnClickListener {
                onAddCategory()
            }
        }
    }

    fun setSelectedCategory(categoryName: String) {
        val position = currentList.indexOfFirst { it.name == categoryName }
        if (position != -1) {
            val previousPosition = selectedPosition
            selectedPosition = position
            if (previousPosition != RecyclerView.NO_POSITION) notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    fun addCategory(category: CategoryEntity) {
        val updatedList = currentList.toMutableList().apply {
            add(category)
        }
        submitList(updatedList) {
            setSelectedCategory(category.name)
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        val position = currentList.indexOfFirst { it.id == category.id }
        if (position != -1) {
            val updatedList = currentList.toMutableList().apply {
                removeAt(position)
            }
            submitList(updatedList) {
                // 삭제된 항목이 선택된 항목이었을 경우 선택 상태 초기화
                if (position == selectedPosition) {
                    selectedPosition = RecyclerView.NO_POSITION
                }
                notifyItemRangeChanged(position, updatedList.size)
            }
        }
    }

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryEntity, isSelected: Boolean) {
            binding.categoryName.text = category.name
            binding.root.setBackgroundColor(if (isSelected) android.graphics.Color.LTGRAY else android.graphics.Color.WHITE)
            binding.deleteBtn.setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
    }

    class AddCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // "카테고리 추가" 항목의 ViewHolder
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryEntity>() {
        override fun areItemsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryEntity, newItem: CategoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}
