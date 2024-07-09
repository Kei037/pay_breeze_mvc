package com.kei037.pay_breeze_mvc.ui.analysis

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.databinding.FragmentIncomeTransactionsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Fragment responsible for displaying and analyzing income transaction data.
 *
 * This fragment provides a user interface for visualizing income data through various
 * time periods (weekly, monthly, quarterly, half-yearly, and yearly) using bar charts
 * and pie charts. It allows users to navigate through different date ranges and view
 * income breakdowns by category.
 *
 * Key features:
 * - Dynamic date range selection (1W, 1M, 3M, 6M, 1Y)
 * - Bar chart visualization of income over time
 * - Pie chart visualization of income by category
 * - Total income calculation for the selected period
 * - Navigation controls for moving between date ranges
 *
 * The fragment interacts with the app's database to fetch transaction data and uses
 * the MPAndroidChart library for rendering charts.
 *
 * See: https://github.com/PhilJay/MPAndroidChart for chart library documentation
 *
 * Note: This implementation assumes that income transactions are represented by
 * positive amounts in the database.
 */

class IncomeTransactionsFragment : Fragment() {
    /**
     * Initializes the fragment's view and sets up UI components.
     *
     * This method is called immediately after onCreateView() and is responsible for:
     * - Initializing chart and UI components
     * - Setting up button click listeners
     * - Selecting the default time range (1W)
     */
    // View binding object for the fragment
    private var _binding: FragmentIncomeTransactionsBinding? = null
    private val binding get() = _binding!!

    // UI components
    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart
    private lateinit var dateRangeTextView: TextView
    private lateinit var totalIncomeTextView: TextView
    private lateinit var button1W: Button
    private lateinit var button1M: Button
    private lateinit var button3M: Button
    private lateinit var button6M: Button
    private lateinit var button1Y: Button
    private lateinit var backButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var pieChartLegend: LinearLayout

    // Current selected tab identifier
    private var currentTab: String? = null

    // Date format and calendar instance for date manipulations
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    // Date range variables
    private var currentWeekStart: Date = getCurrentWeekStart()
    private var currentMonthStart: Date = getCurrentMonthStart()
    private var currentQuarterStart: Date = getCurrentQuarterStart()
    private var currentHalfYearStart: Date = getCurrentHalfYearStart()
    private var currentYearStart: Date = getCurrentYearStart()

    /**
     * Inflates the fragment layout and returns the root view.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIncomeTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after onCreateView. Initializes the UI components and sets up event listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        barChart = binding.barChart
        pieChart = binding.pieChart
        dateRangeTextView = binding.textviewDateRange
        totalIncomeTextView = binding.textviewTotalIncome
        button1W = binding.button1w
        button1M = binding.button1m
        button3M = binding.button3m
        button6M = binding.button6m
        button1Y = binding.button1y
        backButton = binding.buttonBack
        nextButton = binding.buttonNext
        pieChartLegend = binding.pieChartLegend

        // Set up button click listeners
        setupButtonClickListeners()

        // Initially select the 1W button
        button1W.performClick()
    }

    /**
     * Sets up click listeners for time range selection buttons and navigation controls.
     *
     * Each button updates the currentTab and triggers the corresponding update method.
     * Navigation buttons allow moving forward or backward in time based on the selected range.
     */
    private fun setupButtonClickListeners() {
        button1W.setOnClickListener {
            selectButton(button1W)
            currentTab = "1W"
            updateFor1W()
        }
        button1M.setOnClickListener {
            selectButton(button1M)
            currentTab = "1M"
            updateFor1M()
        }
        button3M.setOnClickListener {
            selectButton(button3M)
            currentTab = "3M"
            updateFor3M()
        }
        button6M.setOnClickListener {
            selectButton(button6M)
            currentTab = "6M"
            updateFor6M()
        }
        button1Y.setOnClickListener {
            selectButton(button1Y)
            currentTab = "1Y"
            updateFor1Y()
        }
        backButton.setOnClickListener {
            navigateDateRange(-1)
        }
        nextButton.setOnClickListener {
            navigateDateRange(1)
        }
    }

    /**
     * Updates the UI to reflect the selected time range button.
     *
     * @param selectedButton The button that was selected by the user
     */
    private fun selectButton(selectedButton: Button) {
        val buttons = listOf(button1W, button1M, button3M, button6M, button1Y)
        buttons.forEach { button ->
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        }
        selectedButton.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        selectedButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
    }


    /**
     * Refreshes the data and UI based on the currently selected time range tab.
     *
     * This method is typically called after navigating to a new date range to ensure
     * the displayed data is up-to-date.
     */
    private fun refreshCurrentTab() {
        when (currentTab) {
            "1W" -> updateFor1W()
            "1M" -> updateFor1M()
            "3M" -> updateFor3M()
            "6M" -> updateFor6M()
            "1Y" -> updateFor1Y()
        }
    }

    /**
     * Updates the total income text view based on the fetched transactions.
     * @param transactions The list of income transactions
     */
    private fun updateTotalIncome(transactions: List<TransactionEntity>) {
        val totalIncome = transactions.filter { it.amount > 0 }.sumOf { it.amount }
        totalIncomeTextView.text = "Total Income: $totalIncome"
    }

    /**
     * Updates the data and UI for the weekly (1W) view.
     *
     * This method fetches income data for the current week, updates the bar chart
     * with daily income, updates the pie chart with category-wise income, and
     * displays the total income for the week.
     */
    private fun updateFor1W() {
        lifecycleScope.launch {
            val startDate = currentWeekStart
            val endDate = getDateWithOffset(startDate, 6)

            dateRangeTextView.text = "${dateFormat.format(startDate)} ~ ${dateFormat.format(endDate)}"

            val income = fetchIncomeData(startDate, endDate)
            Log.d("IncomeTransactionsFragment", "Fetched income: $income")

            updateWeeklyBarChart(income)
            updateCategoryPieChart(income)
            updateTotalIncome(income)
        }
    }

    /**
     * Updates the total income text view based on the fetched transactions.
     *
     * @param transactions The list of income transactions for the selected period
     */
    private fun updateFor1M() {
        lifecycleScope.launch {
            val startDate = currentMonthStart
            val endDate = getEndOfMonth(startDate)

            dateRangeTextView.text = "${dateFormat.format(startDate)} ~ ${dateFormat.format(endDate)}"

            val income = fetchIncomeData(startDate, endDate)
            Log.d("IncomeTransactionsFragment", "Fetched income: $income")

            updateMonthlyBarChart(income, startDate, endDate)
            updateCategoryPieChart(income)
            updateTotalIncome(income)
        }
    }



    private fun updateFor3M() {
        lifecycleScope.launch {
            val startDate = currentQuarterStart
            val endDate = getDateWithOffset(startDate, 89)

            dateRangeTextView.text = "${dateFormat.format(startDate)} ~ ${dateFormat.format(endDate)}"

            val income = fetchIncomeData(startDate, endDate)
            Log.d("IncomeTransactionsFragment", "Fetched income: $income")

            updateQuarterlyBarChart(income, startDate, endDate)
            updateCategoryPieChart(income)
            updateTotalIncome(income)
        }
    }

    /**
     * Updates the data and UI for the 6M tab.
     */
    private fun updateFor6M() {
        lifecycleScope.launch {
            val startDate = currentHalfYearStart
            val endDate = getDateWithOffset(startDate, 181)

            dateRangeTextView.text = "${dateFormat.format(startDate)} ~ ${dateFormat.format(endDate)}"

            val income = fetchIncomeData(startDate, endDate)
            Log.d("IncomeTransactionsFragment", "Fetched income: $income")

            updateHalfYearlyBarChart(income, startDate, endDate)
            updateCategoryPieChart(income)
            updateTotalIncome(income)
        }
    }

    /**
     * Updates the data and UI for the 1Y tab.
     */
    private fun updateFor1Y() {
        lifecycleScope.launch {
            val startDate = currentYearStart
            val endDate = getEndOfYear(startDate)

            dateRangeTextView.text = "${dateFormat.format(startDate)} ~ ${dateFormat.format(endDate)}"

            val income = fetchIncomeData(startDate, endDate)
            Log.d("IncomeTransactionsFragment", "Fetched income: $income")

            updateYearlyBarChart(income, startDate, endDate)
            updateCategoryPieChart(income)
            updateTotalIncome(income)
        }
    }

    /**
     * Updates the bar chart with weekly income data.
     *
     * @param transactions The list of income transactions for the week
     */
    private fun updateWeeklyBarChart(transactions: List<TransactionEntity>) {
        val entries = ArrayList<BarEntry>()
        val dayMap = mutableMapOf<String, Float>()

        // Weekdays for the X-axis
        val weekDays = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        // Initialize the map with zero values for each weekday
        weekDays.forEach { day ->
            dayMap[day] = 0f
        }

        // Aggregate the income transactions by day of the week
        transactions.filter { it.amount > 0 }.forEach { transaction ->
            val transactionDate = dateFormat.parse(transaction.transactionDate)
            val dayOfWeek = if (transactionDate != null) {
                SimpleDateFormat("EEEE", Locale.getDefault()).format(transactionDate)
            } else {
                null
            }
            Log.d("IncomeTransactionsFragment", "Transaction date: ${transaction.transactionDate}, Day of week: $dayOfWeek")
            dayOfWeek?.let {
                dayMap[it] = dayMap[it]?.plus(transaction.amount.toFloat()) ?: transaction.amount.toFloat()
            }
        }

        // Prepare the bar entries for the chart
        weekDays.forEachIndexed { index, day ->
            val amount = dayMap[day] ?: 0f
            entries.add(BarEntry(index.toFloat(), amount))
            Log.d("IncomeTransactionsFragment", "Day: $day, Amount: ${dayMap[day] ?: 0f}")
        }

        val dataSet = BarDataSet(entries, "Income")
        val data = BarData(dataSet)

        // Configure the bar chart
        barChart.apply {
            this.data = data
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawLabels(true)
            xAxis.granularity = 1f
            xAxis.labelCount = 7
            xAxis.valueFormatter = IndexAxisValueFormatter(weekDays)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            legend.isEnabled = false
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            setExtraOffsets(0f, 0f, 0f, 0f)
            invalidate() // Refresh the chart
        }
    }

    /**
     * Updates the bar chart with monthly data.
     * @param transactions The list of income transactions
     * @param startDate The start date of the month
     * @param endDate The end date of the month
     */
    private fun updateMonthlyBarChart(transactions: List<TransactionEntity>, startDate: Date, endDate: Date) {
        val entries = ArrayList<BarEntry>()
        val weekMap = mutableMapOf<Int, Float>()
        val weeks = mutableListOf<Int>()

        val calendar = Calendar.getInstance()
        calendar.time = startDate

        // Initialize the map with zero values for each week of the month
        while (calendar.time <= endDate) {
            val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
            weeks.add(weekOfMonth)
            weekMap[weekOfMonth] = 0f
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        // Aggregate the income transactions by week of the month
        transactions.filter { it.amount > 0 }.forEach { transaction ->
            val transactionDate = dateFormat.parse(transaction.transactionDate)
            val weekOfMonth = if (transactionDate != null) {
                val transactionCalendar = Calendar.getInstance()
                transactionCalendar.time = transactionDate
                transactionCalendar.get(Calendar.WEEK_OF_MONTH)
            } else {
                null
            }
            Log.d("IncomeTransactionsFragment", "Transaction date: ${transaction.transactionDate}, Week of month: $weekOfMonth")
            weekOfMonth?.let {
                weekMap[it] = weekMap[it]?.plus(transaction.amount.toFloat()) ?: transaction.amount.toFloat()
            }
        }

        // Prepare the bar entries for the chart
        weeks.forEach { week ->
            val amount = weekMap[week] ?: 0f
            entries.add(BarEntry(week.toFloat(), amount))
            Log.d("IncomeTransactionsFragment", "Week: $week, Amount: $amount")
        }

        val dataSet = BarDataSet(entries, "Income")
        val data = BarData(dataSet)

        // Configure the bar chart
        barChart.apply {
            this.data = data
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawLabels(true)
            xAxis.granularity = 1f
            xAxis.labelCount = weeks.size
            xAxis.valueFormatter = IndexAxisValueFormatter(weeks.map { "Week$it" })
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            legend.isEnabled = false
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            setExtraOffsets(0f, 0f, 0f, 0f)
            invalidate() // Refresh the chart
        }
    }

    /**
     * Updates the bar chart with quarterly data.
     * @param transactions The list of income transactions
     * @param startDate The start date of the quarter
     * @param endDate The end date of the quarter
     */
    private fun updateQuarterlyBarChart(transactions: List<TransactionEntity>, startDate: Date, endDate: Date) {
        val entries = ArrayList<BarEntry>()
        val monthMap = mutableMapOf<Int, Float>()
        val months = mutableListOf<Int>()

        val calendar = Calendar.getInstance()
        calendar.time = startDate

        // Initialize the map with zero values for each month of the quarter
        while (calendar.time <= endDate) {
            val monthOfQuarter = calendar.get(Calendar.MONTH)
            months.add(monthOfQuarter)
            monthMap[monthOfQuarter] = 0f
            calendar.add(Calendar.MONTH, 1)
        }

        // Aggregate the income transactions by month of the quarter
        transactions.filter { it.amount > 0 }.forEach { transaction ->
            val transactionDate = dateFormat.parse(transaction.transactionDate)
            val monthOfQuarter = if (transactionDate != null) {
                val transactionCalendar = Calendar.getInstance()
                transactionCalendar.time = transactionDate
                transactionCalendar.get(Calendar.MONTH)
            } else {
                null
            }
            Log.d("IncomeTransactionsFragment", "Transaction date: ${transaction.transactionDate}, Month of quarter: $monthOfQuarter")
            monthOfQuarter?.let {
                monthMap[it] = monthMap[it]?.plus(transaction.amount.toFloat()) ?: transaction.amount.toFloat()
            }
        }

        // Prepare the bar entries for the chart
        months.forEach { month ->
            val amount = monthMap[month] ?: 0f
            entries.add(BarEntry(month.toFloat(), amount))
            Log.d("IncomeTransactionsFragment", "Month: $month, Amount: $amount")
        }

        val dataSet = BarDataSet(entries, "Income")
        val data = BarData(dataSet)

        val monthLabels = months.map { month ->
            SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.apply { set(Calendar.MONTH, month) }.time)
        }

        // Configure the bar chart
        barChart.apply {
            this.data = data
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawLabels(true)
            xAxis.granularity = 1f
            xAxis.labelCount = months.size
            xAxis.valueFormatter = IndexAxisValueFormatter(monthLabels)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            legend.isEnabled = false
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            setExtraOffsets(0f, 0f, 0f, 0f)
            invalidate() // Refresh the chart
        }
    }

    /**
     * Updates the bar chart with half-yearly data.
     * @param transactions The list of income transactions
     * @param startDate The start date of the half year
     * @param endDate The end date of the half year
     */
    private fun updateHalfYearlyBarChart(transactions: List<TransactionEntity>, startDate: Date, endDate: Date) {
        val entries = ArrayList<BarEntry>()
        val monthMap = mutableMapOf<Int, Float>()
        val months = mutableListOf<Int>()

        val calendar = Calendar.getInstance()
        calendar.time = startDate

        // Collect the six months and initialize the map
        for (i in 0..5) {
            val month = calendar.get(Calendar.MONTH)
            months.add(month)
            monthMap[month] = 0f
            calendar.add(Calendar.MONTH, 1)
        }

        // Aggregate the income transactions by month of the half year
        transactions.filter { it.amount > 0 }.forEach { transaction ->
            val transactionDate = dateFormat.parse(transaction.transactionDate)
            val month = if (transactionDate != null) {
                val transactionCalendar = Calendar.getInstance()
                transactionCalendar.time = transactionDate
                transactionCalendar.get(Calendar.MONTH)
            } else {
                null
            }
            Log.d("IncomeTransactionsFragment", "Transaction date: ${transaction.transactionDate}, Month: $month")
            month?.let {
                monthMap[it] = monthMap[it]?.plus(transaction.amount.toFloat()) ?: transaction.amount.toFloat()
            }
        }

        // Prepare the bar entries for the chart
        months.forEach { month ->
            val amount = monthMap[month] ?: 0f
            entries.add(BarEntry(month.toFloat(), amount))
            Log.d("IncomeTransactionsFragment", "Month: $month, Amount: $amount")
        }

        val dataSet = BarDataSet(entries, "Income")
        val data = BarData(dataSet)

        val monthLabels = months.map { month ->
            SimpleDateFormat("MMMM", Locale.getDefault()).format(Date().apply { calendar.set(Calendar.MONTH, month) })
        }

        // Configure the bar chart
        barChart.apply {
            this.data = data
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawLabels(true)
            xAxis.granularity = 1f
            xAxis.labelCount = 6 // Ensure this is set to 6
            xAxis.valueFormatter = IndexAxisValueFormatter(monthLabels)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            legend.isEnabled = false
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            setExtraOffsets(0f, 0f, 0f, 0f)
            invalidate() // Refresh the chart
        }
    }

    /**
     * Updates the bar chart with yearly data.
     * @param transactions The list of income transactions
     * @param startDate The start date of the year
     * @param endDate The end date of the year
     */
    private fun updateYearlyBarChart(transactions: List<TransactionEntity>, startDate: Date, endDate: Date) {
        val entries = ArrayList<BarEntry>()
        val monthMap = mutableMapOf<Int, Float>()
        val months = mutableListOf<Int>()

        val calendar = Calendar.getInstance()
        calendar.time = startDate

        // Initialize the map with zero values for each month of the year
        while (calendar.time <= endDate) {
            val monthOfYear = calendar.get(Calendar.MONTH)
            months.add(monthOfYear)
            monthMap[monthOfYear] = 0f
            calendar.add(Calendar.MONTH, 1)
        }

        // Aggregate the income transactions by month of the year
        transactions.filter { it.amount > 0 }.forEach { transaction ->
            val transactionDate = dateFormat.parse(transaction.transactionDate)
            val monthOfYear = if (transactionDate != null) {
                val transactionCalendar = Calendar.getInstance()
                transactionCalendar.time = transactionDate
                transactionCalendar.get(Calendar.MONTH)
            } else {
                null
            }
            Log.d("IncomeTransactionsFragment", "Transaction date: ${transaction.transactionDate}, Month of year: $monthOfYear")
            monthOfYear?.let {
                monthMap[it] = monthMap[it]?.plus(transaction.amount.toFloat()) ?: transaction.amount.toFloat()
            }
        }

        // Prepare the bar entries for the chart
        months.forEach { month ->
            val amount = monthMap[month] ?: 0f
            entries.add(BarEntry(month.toFloat(), amount))
            Log.d("IncomeTransactionsFragment", "Month: $month, Amount: $amount")
        }

        val dataSet = BarDataSet(entries, "Income")
        val data = BarData(dataSet)

        val monthLabels = months.map { month ->
            SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.apply { set(Calendar.MONTH, month) }.time)
        }

        // Configure the bar chart
        barChart.apply {
            this.data = data
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawLabels(true)
            xAxis.granularity = 1f
            xAxis.labelCount = months.size
            xAxis.valueFormatter = IndexAxisValueFormatter(monthLabels)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)
            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            legend.isEnabled = false
            description.isEnabled = false
            axisLeft.axisMinimum = 0f
            setExtraOffsets(0f, 0f, 0f, 0f)
            invalidate() // Refresh the chart
        }
    }

    /**
     * Updates the pie chart with category-wise data.
     * @param transactions The list of income transactions
     */
    private fun updateCategoryPieChart(transactions: List<TransactionEntity>) {
        val entries = ArrayList<PieEntry>()
        val categoryMap = mutableMapOf<String, Float>()

        // Aggregate the income transactions by category
        transactions.filter { it.amount > 0 }.forEach { transaction ->
            val amount = transaction.amount.toFloat()
            categoryMap[transaction.categoryName] = categoryMap[transaction.categoryName]?.plus(amount) ?: amount
        }

        // Prepare the pie entries for the chart
        categoryMap.forEach { (category, amount) ->
            entries.add(PieEntry(amount, category))
        }

        val dataSet = PieDataSet(entries, "Income Categories")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val data = PieData(dataSet)

        // Configure the pie chart
        pieChart.apply {
            this.data = data
            description.isEnabled = false
            legend.isEnabled = false
            setUsePercentValues(true)
            invalidate() // Refresh the chart
        }

        // Update legend
        pieChartLegend.removeAllViews()
        categoryMap.forEach { (category, amount) ->
            val color = dataSet.colors[entries.indexOfFirst { it.label == category }]
            val legendItem = createLegendItem(category, amount, color)
            pieChartLegend.addView(legendItem)
        }
    }

    /**
     * Creates a legend item view for the pie chart.
     * @param category The category name
     * @param amount The total amount for the category
     * @param color The color associated with the category
     * @return The legend item view
     */
    private fun createLegendItem(category: String, amount: Float, color: Int): View {
        val legendItem = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 8, 0, 8)
        }

        val colorSquare = View(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(24, 24).apply {
                rightMargin = 16
            }
            setBackgroundColor(color)
        }

        val textView = TextView(requireContext()).apply {
            text = "$category - ${String.format("%.2f", amount)}"
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        }

        legendItem.addView(colorSquare)
        legendItem.addView(textView)
        return legendItem
    }

    /**
     * Fetches the income data for the specified date range.
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return The list of income transactions
     */
    private suspend fun fetchIncomeData(startDate: Date, endDate: Date): List<TransactionEntity> {
        return withContext(Dispatchers.IO) {
            val db = AppDatabase.getInstance(requireContext())
            db.getTransactionDao().getTransactionsByDateRange(
                dateFormat.format(startDate),
                dateFormat.format(endDate)
            ).also {
                Log.d("IncomeTransactionsFragment", "DB Query Result: $it")
            }
        }
    }

    /**
     * Navigates the date range based on the provided units.
     * @param units The number of units to navigate (positive for forward, negative for backward)
     */
    private fun navigateDateRange(units: Int) {
        when (currentTab) {
            "1W" -> {
                calendar.time = currentWeekStart
                calendar.add(Calendar.WEEK_OF_YEAR, units)
                currentWeekStart = calendar.time
                updateFor1W()
            }
            "1M" -> {
                calendar.time = currentMonthStart
                calendar.add(Calendar.MONTH, units)
                currentMonthStart = calendar.time
                updateFor1M()
            }
            "3M" -> {
                calendar.time = currentQuarterStart
                calendar.add(Calendar.MONTH, units * 3)
                currentQuarterStart = calendar.time
                updateFor3M()
            }
            "6M" -> {
                calendar.time = currentHalfYearStart
                calendar.add(Calendar.MONTH, units * 6)
                currentHalfYearStart = calendar.time
                updateFor6M()
            }
            "1Y" -> {
                calendar.time = currentYearStart
                calendar.add(Calendar.YEAR, units)
                currentYearStart = calendar.time
                updateFor1Y()
            }
        }
    }

    private fun getCurrentWeekStart(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return calendar.time
    }

    private fun getCurrentMonthStart(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }

    private fun getCurrentQuarterStart(): Date {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val quarterStartMonth = month - month % 3
        calendar.set(Calendar.MONTH, quarterStartMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }

    private fun getCurrentHalfYearStart(): Date {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val halfYearStartMonth = if (month >= Calendar.JULY) Calendar.JULY else Calendar.JANUARY
        calendar.set(Calendar.MONTH, halfYearStartMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }

    private fun getCurrentYearStart(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        return calendar.time
    }

    private fun getDateWithOffset(startDate: Date, offset: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.add(Calendar.DAY_OF_YEAR, offset)
        return calendar.time
    }

    private fun getEndOfMonth(startDate: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return calendar.time
    }

    private fun getEndOfYear(startDate: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
        return calendar.time
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
