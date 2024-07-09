package com.kei037.pay_breeze_mvc.ui.analysis

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AnalysisPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpenseTransactionsFragment()
            1 -> IncomeTransactionsFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}

