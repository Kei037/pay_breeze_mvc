package com.kei037.pay_breeze_mvc.ui.calender

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kei037.pay_breeze_mvc.data.db.dao.CategoryDao
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.databinding.FragmentCalenderBinding
import com.kei037.pay_breeze_mvc.databinding.FragmentHomeBinding

class CalenderFragment : Fragment() {

    // viewBinding 초기화
    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!

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



    }

    /**
     * 화면 전환시 viewBinding 해제
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}