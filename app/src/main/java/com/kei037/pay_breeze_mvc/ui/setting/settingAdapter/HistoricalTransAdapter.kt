package com.kei037.pay_breeze_mvc.ui.setting.settingAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.databinding.ItemEventBinding
import com.kei037.pay_breeze_mvc.ui.commons.DetailedActivity

class HistoricalTransAdapter :
    ListAdapter<TransactionEntity, HistoricalTransAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)

        // 항목 클릭 리스너 추가
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailedActivity::class.java).apply {
                putExtra("EVENT_DETAIL", transaction.toString())
            }
            context.startActivity(intent)
        }
    }

    class TransactionViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: TransactionEntity) {
            binding.titleTextView.text = transaction.title
            binding.categoryTextView.text = transaction.categoryName // 카테고리 이름 설정
            binding.amountTextView.text = transaction.amount.toString()

            // 금액이 양수일 때 customBlue 색상으로 설정
            if (transaction.amount > 0) {
                binding.amountTextView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.customBlue))
            } else {
                binding.amountTextView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            }
        }
    }
}
