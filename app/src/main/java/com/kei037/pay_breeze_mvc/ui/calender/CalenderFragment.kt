package com.kei037.pay_breeze_mvc.ui.calender

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kei037.pay_breeze_mvc.data.db.dao.CategoryDao
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.databinding.FragmentCalenderBinding
import com.kei037.pay_breeze_mvc.databinding.FragmentHomeBinding

class CalenderFragment : Fragment() {

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


        // 뷰 바인딩을 사용하여 뷰 요소에 접근
        binding.saveBtn.setOnClickListener {
            val inputText = binding.inputEditText.text.toString()
            binding.textView.text = inputText
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