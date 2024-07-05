package com.kei037.pay_breeze_mvc.ui.commons

import java.text.NumberFormat
import java.util.Locale

object Utils {
    fun formatDouble(value: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        formatter.maximumFractionDigits = 0
        return formatter.format(value)
    }
}