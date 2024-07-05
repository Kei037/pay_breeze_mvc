package com.kei037.pay_breeze_mvc.ui.setting.settingAdapter

import androidx.recyclerview.widget.DiffUtil
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity

class TransactionDiffCallback : DiffUtil.ItemCallback<TransactionEntity>() {
    override fun areItemsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity): Boolean {
        return oldItem == newItem
    }
}
