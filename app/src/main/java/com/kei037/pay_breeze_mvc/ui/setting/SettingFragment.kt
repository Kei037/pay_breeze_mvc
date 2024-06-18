package com.kei037.pay_breeze_mvc.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.dao.CategoryDao
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.databinding.FragmentCalenderBinding
import com.kei037.pay_breeze_mvc.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    // viewBinding 초기화
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    // DAO 초기화
    private lateinit var categoryDao: CategoryDao

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

        // Room 데이터베이스 인스턴스 초기화
        val db = AppDatabase.getInstance(requireContext())
        categoryDao = db.getCategoryDao()

        // 저장 버튼 클릭 리스너 설정
        binding.saveBtn.setOnClickListener {
            val inputText = binding.inputEditText.text.toString()
            val newCategory = CategoryEntity(name = inputText, isPublic = true)

            // 데이터베이스 작업은 백그라운드 스레드에서 수행
            Thread {
                categoryDao.insertCategory(newCategory)
                // UI 업데이트는 메인 스레드에서 수행
                requireActivity().runOnUiThread {
                    binding.textView.text = "Saved: $inputText"
                }
            }.start()
        }

        // 데이터 가져오기 예제
        binding.loadBtn.setOnClickListener {
            Thread {
                val categories = categoryDao.getAll()
                requireActivity().runOnUiThread {
                    binding.textView.text = categories.joinToString { it.name }
                }
            }.start()
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