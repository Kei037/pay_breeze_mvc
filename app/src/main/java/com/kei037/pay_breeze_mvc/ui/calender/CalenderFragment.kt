package com.kei037.pay_breeze_mvc.ui.calender

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.kei037.pay_breeze_mvc.data.db.dao.CategoryDao
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.databinding.FragmentCalenderBinding
import com.kei037.pay_breeze_mvc.databinding.FragmentHomeBinding
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalenderFragment : Fragment() {

    // viewBinding 초기화
    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EventAdapter
    private lateinit var db: AppDatabase
    private lateinit var calendarView: MaterialCalendarView

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

        db = AppDatabase.getInstance(requireContext())
        adapter = EventAdapter(listOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        calendarView = binding.calendarView

        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_RANGE

        // 단일 날짜 선택
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            Log.i("날짜 == ", date.toString())
            Log.i("선택 == ", selected.toString())
        })
        // 기간 날짜 선택
        calendarView.setOnRangeSelectedListener(OnRangeSelectedListener { widget, dates ->
            Log.i("날짜 == ", dates.toString())
        })

//        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
//            val calender = Calendar.getInstance()
//            calender.set(year, month, dayOfMonth)
//            val selectedDate = calender.time
//            loadEventsForDate(selectedDate)
//        }

        // 초기화 시 오늘 날짜 이벤트 로드
        loadEventsForDate(Date())
    }

    private fun loadEventsForDate(date: Date) {
        CoroutineScope(Dispatchers.IO).launch {
            val transactions = db.getTransactionDao().getAll()
            withContext(Dispatchers.Main) {
                adapter.updateEvents(transactions)
            }
        }
    }

    /**
     * 화면 전환시 viewBinding 해제
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}