package com.kei037.pay_breeze.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "title")
    val title:String,

    @ColumnInfo(name = "amount")
    val amount:Double,

    @ColumnInfo(name = "transaction_date")
    val transactionDate: Date,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "category_name")
    val categoryName: String
)