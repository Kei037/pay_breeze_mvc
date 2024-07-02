package com.kei037.pay_breeze_mvc.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.dao.CategoryDao
import com.kei037.pay_breeze_mvc.data.db.dao.TransactionDao
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.databinding.FragmentCalenderBinding
import com.kei037.pay_breeze_mvc.databinding.FragmentSettingBinding
import java.time.LocalDateTime
import java.util.Date

class SettingFragment : Fragment() {
    // viewBinding 초기화
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    // DAO 초기화
    private lateinit var transactionDao: TransactionDao
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
        transactionDao = db.getTransactionDao()
        categoryDao = db.getCategoryDao()

        // 저장 버튼 클릭 리스너 설정
        binding.saveBtn.setOnClickListener {
            val titleText = binding.titleEditText.text.toString()
            val amount = binding.amountEditText.text.toString()
            val date = binding.dateEditText.text.toString()
            val des = binding.desEditText.text.toString()
            val ctName = binding.cateEditText.text.toString()
            val newTrans = TransactionEntity(null, titleText, amount.toDouble(), date, des, ctName)

            val category = CategoryEntity(null, ctName, false)

            // 데이터베이스 작업은 백그라운드 스레드에서 수행
            Thread {
                transactionDao.insertTransaction(newTrans)
                categoryDao.insertCategory(category)
                // UI 업데이트는 메인 스레드에서 수행
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "저장됨!!", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }

        // 데이터 가져오기 예제
//        binding.loadBtn.setOnClickListener {
//            Thread {
//                val categories = categoryDao.getAll()
//                requireActivity().runOnUiThread {
//                    binding.textView.text = categories.joinToString { it.name }
//                }
//            }.start()
//        }
    }

    /**
     * 화면 전환시 viewBinding 해제
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}