package com.kei037.pay_breeze_mvc.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.databinding.FragmentHomeBinding
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.EventAdapter
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
                params.setMargins(0, 36, 0, 0)
                buttonLayout.layoutParams = params
            }

            if (alpha.toInt() == 0) {
                params.setMargins(0, 0, 0, 0)
                buttonLayout.layoutParams = params
            }
        })
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
