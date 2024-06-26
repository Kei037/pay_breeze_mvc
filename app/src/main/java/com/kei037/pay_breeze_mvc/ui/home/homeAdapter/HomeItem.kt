package com.kei037.pay_breeze_mvc.ui.home.homeAdapter

import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.ListItem
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.ViewType

data class HomeItem (
    val title: String,
    val categoryName: String,
    val amount: Double,
    val transaction: TransactionEntity
) : ListItem {
    override fun getType() = ViewType.HOME_ITEM
}