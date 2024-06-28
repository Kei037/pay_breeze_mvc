package com.kei037.pay_breeze_mvc.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.databinding.FragmentHomeBinding
import com.kei037.pay_breeze_mvc.ui.commons.commonsAdapter.EventAdapter
import com.kei037.pay_breeze_mvc.ui.home.homeAdapter.HomeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var twoCl: ConstraintLayout
    private lateinit var buttonLayout: LinearLayout
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter
    private var db: AppDatabase? = null

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

        db = AppDatabase.getInstance(requireContext())

        loadTransactionsFromDatabase()

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

    // 수입/지출 필터
    private fun showPopupMenu1(view: View, chip: Chip) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_home, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_one -> {
                    chip.text = menuItem.title
                    // Action for menu item one
                    true

                }
                R.id.action_two -> {
                    chip.text = menuItem.title
                    // Action for menu item two
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    // 기간 설정 필터
    private fun showPopupMenu2(view: View, chip: Chip) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_home2, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_one -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_two -> {
                    chip.text = menuItem.title
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    // 카테고리 설정 필터
    private fun showPopupMenu3(view: View, chip: Chip) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_home3, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_one -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_two -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_three -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_4 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_5 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_6 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_7 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_8 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_9 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_10 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_11 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_12 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_13 -> {
                    chip.text = menuItem.title
                    true
                }
                R.id.action_14 -> {
                    chip.text = menuItem.title
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    private fun loadTransactionsFromDatabase() {
        lifecycleScope.launch {
            try {
                val transactionEntities = withContext(Dispatchers.IO) {
                    db?.getTransactionDao()?.getAll() ?: emptyList()
                }

                val eventItems = transactionEntities.map { transaction ->
                    HomeItem(
                        title = transaction.title,
                        categoryName = transaction.categoryName,
                        amount = transaction.amount,
                        transaction
                    )
                }

                adapter.updateEvents(eventItems)
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
