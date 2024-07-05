package com.kei037.pay_breeze_mvc.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kei037.pay_breeze_mvc.databinding.FragmentSettingBinding
import com.kei037.pay_breeze_mvc.ui.commons.EditCategoryActivity

class SettingFragment : Fragment() {
    // viewBinding 초기화
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    /**
     * 처음 화면을 실행시 viewBinding 초기화
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goEditCategory.setOnClickListener {
            val intent = Intent(activity, CustomCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.goHistoricalTrans.setOnClickListener {
            val intent = Intent(activity, HistoricalTransActivity::class.java)
            startActivity(intent)
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
