package com.kei037.pay_breeze_mvc.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.databinding.FragmentAnalysisBinding

class AnalysisFragment : Fragment() {

    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        val view = binding.root

        // 데이터베이스 초기화
        db = AppDatabase.getInstance(requireContext())

        // 데이터 로드 및 그래프 설정
        loadDataAndSetupCharts()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadDataAndSetupCharts() {
        // Room 데이터베이스에서 데이터를 로드합니다 (예: Coroutine을 사용할 수 있습니다)
        Thread {
            val transactions = db.getTransactionDao().getAll()

            // 데이터가 없는 경우 로그 출력
            if (transactions.isEmpty()) {
                requireActivity().runOnUiThread {
                    // 데이터가 없음을 사용자에게 알리는 처리를 추가할 수 있습니다.
                }
                return@Thread
            }

            val updatedTransactions = db.getTransactionDao().getAll()

            // 카테고리별 수입/지출 데이터 생성
            val categoryData = updatedTransactions.groupBy { it.categoryName }
                .mapValues { entry -> entry.value.sumByDouble { it.amount } }

            // 주간별 지출 데이터 생성 (임시 로직, 실제로는 날짜별로 그룹화 필요)
            val weeklyData = updatedTransactions.groupBy { it.transactionDate.substring(0, 7) }
                .mapValues { entry -> entry.value.sumByDouble { it.amount } }

            // 그래프에 데이터 설정
            requireActivity().runOnUiThread {
                setupCategoryBarChart(categoryData)
                setupCategoryPieChart(categoryData)
                setupWeeklyLineChart(weeklyData)
            }
        }.start()
    }

    private fun setupCategoryBarChart(categoryData: Map<String, Double>) {
        val entries = categoryData.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }
        val dataSet = BarDataSet(entries, "카테고리별 수입/지출")
        dataSet.color = resources.getColor(R.color.teal_200)
        val barData = BarData(dataSet)

        binding.categoryBarChart.data = barData
        binding.categoryBarChart.description.text = "카테고리별 수입/지출"
        binding.categoryBarChart.setFitBars(true)

        // X축 레이블 설정
        val xAxis = binding.categoryBarChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(categoryData.keys.toList())
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        // Y축 설정
        val leftAxis = binding.categoryBarChart.axisLeft
        leftAxis.axisMinimum = 0f

        val rightAxis = binding.categoryBarChart.axisRight
        rightAxis.isEnabled = false

        binding.categoryBarChart.invalidate()
    }

    private fun setupCategoryPieChart(categoryData: Map<String, Double>) {
        val entries = categoryData.entries.map { entry ->
            PieEntry(entry.value.toFloat(), entry.key)
        }
        val dataSet = PieDataSet(entries, "카테고리별 수입/지출")
        dataSet.colors = listOf(
            resources.getColor(R.color.teal_200),
            resources.getColor(R.color.teal_700),
            resources.getColor(R.color.purple_200),
            resources.getColor(R.color.purple_500),
            resources.getColor(R.color.purple_700)
        )
        val pieData = PieData(dataSet)

        binding.categoryPieChart.data = pieData
        binding.categoryPieChart.description.text = "카테고리별 수입/지출"
        binding.categoryPieChart.setUsePercentValues(true)
        binding.categoryPieChart.invalidate()
    }

    private fun setupWeeklyLineChart(weeklyData: Map<String, Double>) {
        val entries = weeklyData.entries.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.value.toFloat())
        }
        val dataSet = LineDataSet(entries, "주간별 지출")
        dataSet.color = resources.getColor(R.color.teal_200)
        dataSet.setCircleColor(resources.getColor(R.color.teal_200))
        val lineData = LineData(dataSet)

        binding.weeklyLineChart.data = lineData
        binding.weeklyLineChart.description.text = "주간별 지출"

        // X축 레이블 설정
        val xAxis = binding.weeklyLineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(weeklyData.keys.toList())
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        // Y축 설정
        val leftAxis = binding.weeklyLineChart.axisLeft
        leftAxis.axisMinimum = 0f

        val rightAxis = binding.weeklyLineChart.axisRight
        rightAxis.isEnabled = false

        binding.weeklyLineChart.invalidate()
    }
}
