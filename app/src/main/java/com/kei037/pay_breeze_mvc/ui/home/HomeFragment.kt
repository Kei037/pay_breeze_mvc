package com.kei037.pay_breeze_mvc.ui.home

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.repository.TransactionRepository
import com.kei037.pay_breeze_mvc.databinding.FragmentHomeBinding
import com.kei037.pay_breeze_mvc.ui.commons.Utils
import com.kei037.pay_breeze_mvc.ui.commons.commonsAdapter.EventAdapter
import com.kei037.pay_breeze_mvc.ui.home.homeAdapter.HomeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    /* 투명도 조절 */
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var twoCl: ConstraintLayout
    private lateinit var buttonLayout: LinearLayout
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventAdapter

    private lateinit var repository: TransactionRepository

    private lateinit var allTransactions: List<HomeItem>

    private var currentType: String? = null
    private var currentCategory: String? = null
    private var currentPeriod: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBarLayout = binding.appBarLayout
        twoCl = binding.twoCl
        buttonLayout = binding.buttonLayout

        val eventRecyclerView: RecyclerView = binding.recyclerView
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = EventAdapter(emptyList(), requireContext())
        eventRecyclerView.adapter = adapter

        repository = TransactionRepository(requireContext())

        loadTransactionsFromDatabase()
        updateTransactionViews()

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val alpha = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()
            twoCl.alpha = alpha

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            if (alpha.toInt() == 1) {
                params.setMargins(0, 32, 0, 0)
                buttonLayout.layoutParams = params
            }

            if (alpha.toInt() == 0) {
                params.setMargins(0, 0, 0, 0)
                buttonLayout.layoutParams = params
            }
        })

        // Chip 클릭 리스너 설정
        val menuChip1: Chip = binding.cashChip
        menuChip1.setOnClickListener {
            showPopupMenu1(it, menuChip1)
        }

        val menuChip2: Chip = binding.dateChip
        menuChip2.setOnClickListener {
            showPopupMenu2(it, menuChip2)
        }

        val menuChip3: Chip = binding.categoryChip
        menuChip3.setOnClickListener {
            showPopupMenu3(it, menuChip3)
        }
    }



    // 필터링 함수
    private fun applyFilters() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val filteredTransactions = allTransactions.filter {
            val typeMatches = when (currentType) {
                "수입" -> it.transaction.amount > 0
                "지출" -> it.transaction.amount < 0
                else -> true
            }
            val categoryMatches = currentCategory?.let { cat -> it.transaction.categoryName == cat } ?: true
            val periodMatches = when (currentPeriod) {
                "1주일" -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DATE, -7)
                    val weekAgo = calendar.time
                    val transactionDate = dateFormat.parse(it.transaction.transactionDate)
                    transactionDate.after(weekAgo)
                }
                "1개월" -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.MONTH, -1)
                    val monthAgo = calendar.time
                    val transactionDate = dateFormat.parse(it.transaction.transactionDate)
                    transactionDate.after(monthAgo)
                }
                "3개월" -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.MONTH, -3)
                    val monthAgo = calendar.time
                    val transactionDate = dateFormat.parse(it.transaction.transactionDate)
                    transactionDate.after(monthAgo)
                }
                "6개월" -> {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.MONTH, -6)
                    val monthAgo = calendar.time
                    val transactionDate = dateFormat.parse(it.transaction.transactionDate)
                    transactionDate.after(monthAgo)
                }
                else -> true
            }
            typeMatches && categoryMatches && periodMatches
        }
        adapter.updateEvents(filteredTransactions)
    }



    // 수입/지출 필터
    private fun showPopupMenu1(view: View, chip: Chip) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.transactionType_none -> {
                    chip.text = "거래종류"
                    currentType = null
                }
                R.id.transactionType_1 -> {
                    chip.text = menuItem.title
                    currentType = "수입"
                }
                R.id.transactionType_2 -> {
                    chip.text = menuItem.title
                    currentType = "지출"
                }
                else -> false
            }
            applyFilters()
            true
        }
        popupMenu.show()
    }

    // 기간 필터
    private fun showPopupMenu2(view: View, chip: Chip) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_home2, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.date_none -> {
                    chip.text = "기간"
                    currentPeriod = null
                }
                R.id.date_1 -> {
                    chip.text = menuItem.title
                    currentPeriod = "1주일"
                }
                R.id.date_2 -> {
                    chip.text = menuItem.title
                    currentPeriod = "1개월"
                }
                R.id.date_3 -> {
                    chip.text = menuItem.title
                    currentPeriod = "3개월"
                }
                R.id.date_2 -> {
                    chip.text = menuItem.title
                    currentPeriod = "6개월"
                }
                else -> false
            }
            applyFilters()
            true
        }
        popupMenu.show()
    }

    // 카테고리 필터
    private fun showPopupMenu3(view: View, chip: Chip) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_home3, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.category_none -> {
                    chip.text = "카테고리"
                    currentCategory = null
                }
                R.id.category_1 -> {
                    chip.text = menuItem.title
                    currentCategory = menuItem.title.toString()
                }
                R.id.category_2 -> {
                    chip.text = menuItem.title
                    currentCategory = menuItem.title.toString()
                }
                R.id.category_3 -> {
                    chip.text = menuItem.title
                    currentCategory = menuItem.title.toString()
                }
                // Add more categories as needed
                else -> false
            }
            applyFilters()
            true
        }
        popupMenu.show()
    }


    private fun loadTransactionsFromDatabase() {
        lifecycleScope.launch {
            try {
                val transactionEntities = withContext(Dispatchers.IO) {
                    repository.getAllTransactions()
                }

                allTransactions = transactionEntities.map { transaction ->
                    HomeItem(
                        title = transaction.title,
                        categoryName = transaction.categoryName,
                        amount = transaction.amount,
                        transaction
                    )
                }

                adapter.updateEvents(allTransactions)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // total, income, expenses 불러오기
    private fun updateTransactionViews() {
        lifecycleScope.launch {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val todayDate = dateFormat.format(Date())

                val income = withContext(Dispatchers.IO) {
                    repository.getTotalIncomeByDate(todayDate)
                }
                val expense = withContext(Dispatchers.IO) {
                    repository.getTotalExpenseByDate(todayDate)
                }
                val total = income + expense

                binding.miniTotal.text = Utils.formatDouble(total)
                binding.miniIn.text = Utils.formatDouble(income)
                binding.miniEx.text = Utils.formatDouble(expense)

                binding.todaysTotal.text = Utils.formatDouble(total)
                binding.todaysIncome.text = Utils.formatDouble(income)
                binding.todaysExpenses.text = Utils.formatDouble(expense)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
