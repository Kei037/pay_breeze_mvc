package com.kei037.pay_breeze_mvc.ui.calender

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.databinding.FragmentCalenderBinding
import com.kei037.pay_breeze_mvc.ui.calender.dacorators.MiddleDateDecorator
import com.kei037.pay_breeze_mvc.ui.calender.dacorators.SingleDateDecorator
import com.kei037.pay_breeze_mvc.ui.calender.dacorators.StartEndDateDecorator
import com.kei037.pay_breeze_mvc.ui.commons.commonsAdapter.EventAdapter
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.groupEventsByDate
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import java.text.SimpleDateFormat
import java.util.Date

class CalenderFragment : Fragment() {

    // viewBinding 초기화
    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventAdapter
    private lateinit var calendarView: MaterialCalendarView

    // 날짜 선택시 임시 변수
    private var rangeStart: CalendarDay? = null
    private var rangeEnd: CalendarDay? = null

    // db 초기화
    private var db: AppDatabase? = null

    /**
     * 처음 화면을 실행시 viewBinding 초기화
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * View가 생성된 후 호출되어 초기화 작업을 수행
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB 연결
        db = AppDatabase.getInstance(requireContext())

        // UI 및 데이터 초기화
        setupRecyclerView()
        setupCalendarView()
        selectTodayDate()
    }

    // 화면이 다시 활성화될 때 데이터를 새로 고침
    override fun onResume() {
        super.onResume()
        refreshData()
    }

    /**
     * RecyclerView를 설정하는 함수
     */
    private fun setupRecyclerView() {
        adapter = EventAdapter(listOf(), requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    /**
     * CalendarView를 설정하는 함수
     */
    private fun setupCalendarView() {
        calendarView = binding.calendarView
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_RANGE

        // 단일 날짜 선택 리스너 설정
        calendarView.setOnDateChangedListener { _, date, _ ->
            rangeStart = date
            rangeEnd = date
            updateDayDecorator()
            loadEventsForDate(changeLocalDateToDate(date.date))
        }

        // 날짜 범위 선택 리스너 설정
        calendarView.setOnRangeSelectedListener { _, dates ->
            rangeStart = dates.first()
            rangeEnd = dates.last()
            updateRangeDecorator()
            loadEventsForRange(changeLocalDateToDate(dates.first().date), changeLocalDateToDate(dates.last().date))
        }
    }

    /**
     * 오늘 날짜를 선택하고 초기화하는 함수
     */
    private fun selectTodayDate() {
        val today = CalendarDay.today()
        calendarView.setDateSelected(today, true)
        rangeStart = today
        rangeEnd = today
        loadEventsForDate(SimpleDateFormat("yyyy-MM-dd").format(Date()))
        updateDayDecorator()
    }

    /**
     * 화면 재활성화 시 데이터 새로 고침
     */
    private fun refreshData() {
        rangeStart?.let { start ->
            rangeEnd?.let { end ->
                if (start == end) {
                    loadEventsForDate(changeLocalDateToDate(start.date))
                    updateDayDecorator()
                } else {
                    loadEventsForRange(changeLocalDateToDate(start.date), changeLocalDateToDate(end.date))
                    updateRangeDecorator()
                }
            }
        }
    }

    /**
     * org.threeten.bp.LocalDate 타입을 yyyy-MM-dd 형식의 String으로 변경
     * @param localDate
     * @return String
     */
    private fun changeLocalDateToDate(localDate: LocalDate): String {
        val localDateTime = localDate.atStartOfDay()
        val zonedDateTime = localDateTime.atZone(ZoneId.systemDefault())
        val instant = zonedDateTime.toInstant()
        val date = Date(instant.toEpochMilli())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(date)
    }

    /**
     * 날짜 설정시 가계부 출력
     * @param dateString 날짜
     */
    private fun loadEventsForDate(dateString: String) {
        lifecycleScope.launch {
            try {
                val transactions = withContext(Dispatchers.IO) {
                    db?.getTransactionDao()?.getTransactionsByDate(dateString)
                }
                val groupedItems = transactions?.let { groupEventsByDate(it) }
                groupedItems?.let { adapter.updateEvents(it) }
            } catch (e: Exception) {
                Log.e("Error loading events", e.message.toString())
            }
        }
    }

    /**
     * 기간 설정시 가계부 출력
     * @param startDate 시작 날짜
     * @param endDate 끝 날짜
     */
    private fun loadEventsForRange(startDate: String, endDate: String) {
        lifecycleScope.launch {
            try {
                val transactions = withContext(Dispatchers.IO) {
                    db?.getTransactionDao()?.getTransactionsByDateRange(startDate, endDate)
                }
                val groupedItems = transactions?.let { groupEventsByDate(it) }
                groupedItems?.let { adapter.updateEvents(it) }
            } catch (e: Exception) {
                Log.e("Error loading events", e.message.toString())
            }
        }
    }

    /**
     * 날짜 하루 선택 색상 변경
     */
    private fun updateDayDecorator() {
        calendarView.removeDecorators()
        rangeStart?.let {
            calendarView.addDecorator(SingleDateDecorator(it, requireContext(), R.drawable.selection_background))
        }
    }

    /**
     * 날짜 기간 선택 색상 변경
     */
    private fun updateRangeDecorator() {
        calendarView.removeDecorators()
        rangeStart?.let {
            calendarView.addDecorator(StartEndDateDecorator(it, requireContext(), R.drawable.start_date_background))
        }
        rangeEnd?.let {
            calendarView.addDecorator(StartEndDateDecorator(it, requireContext(), R.drawable.end_date_background))
        }
        calendarView.addDecorator(MiddleDateDecorator(rangeStart, rangeEnd, requireContext(), R.drawable.middle_date_background))
    }

    /**
     * 화면 전환시 viewBinding 해제
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
