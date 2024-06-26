package com.kei037.pay_breeze_mvc.ui.calender

import android.content.Intent
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
import com.kei037.pay_breeze_mvc.ui.MiddleDateDecorator
import com.kei037.pay_breeze_mvc.ui.SingleDateDecorator
import com.kei037.pay_breeze_mvc.ui.StartEndDateDecorator
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.EventAdapter
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.EventItem
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.groupEventsByDate
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import org.threeten.bp.ZoneId
import java.util.Date

class CalenderFragment : Fragment() {

    // viewBinding 초기화
    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventAdapter
    private lateinit var calendarView: MaterialCalendarView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DB 연결
        db = AppDatabase.getInstance(requireContext())

        // 가계부 리스트 받아오기
        adapter = EventAdapter(listOf(), requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // 캘린더 뷰 바인딩
        calendarView = binding.calendarView

        // MaterialCalendarView를 기간도 설정 가능하게 변경
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_RANGE

        // 단일 날짜 선택
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            // 캘린더 색상 변경
            rangeStart = date
            rangeEnd = date
            updateDayDecorator()

            Log.i("선택 날짜 == ", date.toString())
            Log.i("선택 == ", selected.toString())
            Log.i("바뀐 날짜 == ", changeLocalDateToDate(date.date).toString())

            // 리스트 업데이트
            loadEventsForDate(changeLocalDateToDate(date.date))
        })

        // 기간 날짜 선택
        calendarView.setOnRangeSelectedListener(OnRangeSelectedListener { widget, dates ->
            // 캘린더 색상 변경
            rangeStart = dates.first()
            rangeEnd = dates.last()
            updateRangeDecorator()

            Log.i("날짜 == ", dates.toString())
            // 리스트 업데이트
            loadEventsForRange(changeLocalDateToDate(dates.first().date), changeLocalDateToDate(dates.last().date))
        })

        // 오늘 날짜 가져오기
        val today = CalendarDay.today()

        // 오늘 날짜 선택하기
        calendarView.setDateSelected(today, true)
        rangeStart = today
        rangeEnd = today

        // 초기화 시 오늘 날짜 이벤트 로드
        loadEventsForDate(SimpleDateFormat("yyyy-MM-dd").format(Date()))
        updateDayDecorator()
    }

    // 화면이 다시 활성화될 때 데이터를 새로 고침
    override fun onResume() {
        super.onResume()
        // 화면이 다시 활성화될 때 데이터를 새로 고침
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart == rangeEnd) {
                // 단일 날짜 선택
                loadEventsForDate(changeLocalDateToDate(rangeStart!!.date))
                updateDayDecorator()
            } else {
                // 기간 날짜 선택
                loadEventsForRange(changeLocalDateToDate(rangeStart!!.date), changeLocalDateToDate(rangeEnd!!.date))
                updateRangeDecorator()
            }
        }
    }

    /**
     * org.threeten.bp.LocalDate 타입을 yyyy-MM-dd 형식의 String으로 변경
     * @param localDate
     * @return String
     */
    private fun changeLocalDateToDate(localDate: LocalDate): String {
        // LocalDate를 LocalDateTime으로 변환
        val localDateTime = localDate.atStartOfDay()
        // LocalDateTime을 ZonedDateTime으로 변환
        val zonedDateTime = localDateTime.atZone(ZoneId.systemDefault())
        // ZonedDateTime을 Instant로 변환
        val instant = zonedDateTime.toInstant()
        // Instant를 Date로 변환
        val date = Date(instant.toEpochMilli())

        // Date를 원하는 형식으로 변환하여 출력
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = dateFormat.format(date)

        return formattedDate
    }


    /**
     * 날짜 설정시 가계부 출력
     * @param dateString 날짜
     */
    private fun loadEventsForDate(dateString: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val transactions = db?.getTransactionDao()?.getTransactionsByDate(dateString)
            val groupedItems = transactions?.let { groupEventsByDate(it) }
            withContext(Dispatchers.Main) {
                if (groupedItems != null) {
                    adapter.updateEvents(groupedItems)
                }
            }
        }
    }

    /**
     * 기간 설정시 가계부 출력
     * @param startDate 시작 날짜
     * @param endDate 끝 날짜
     */
    private fun loadEventsForRange(startDate: String, endDate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val transactions =
                db?.getTransactionDao()?.getTransactionsByDateRange(startDate, endDate)
            val groupedItems = transactions?.let { groupEventsByDate(it) }
            withContext(Dispatchers.Main) {
                if (groupedItems != null) {
                    adapter.updateEvents(groupedItems)
                }
            }
        }
    }


    /**
     * 날짜 하루 선택 색상 변경
     */
    private fun updateDayDecorator() {
        calendarView.removeDecorators()
        rangeStart?.let {
            calendarView.addDecorator(
                SingleDateDecorator(it, requireContext(),
                R.drawable.selection_background
            ))
        }
    }


    /**
     * 날짜 기간 선택 색상 변경
     */
    private fun updateRangeDecorator() {
        calendarView.removeDecorators()
        rangeStart?.let {
            calendarView.addDecorator(StartEndDateDecorator(it, requireContext(),
                R.drawable.start_date_background
            ))
        }
        rangeEnd?.let {
            calendarView.addDecorator(StartEndDateDecorator(it, requireContext(),
                R.drawable.end_date_background
            ))
        }
        calendarView.addDecorator(MiddleDateDecorator(rangeStart, rangeEnd, requireContext(),
            R.drawable.middle_date_background))
    }

    /**
     * 화면 전환시 viewBinding 해제
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}