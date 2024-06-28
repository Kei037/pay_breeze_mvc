package com.kei037.pay_breeze_mvc.ui.addition

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.databinding.FragmentAdditionBinding

/**
 * 트랜잭션 추가 및 관리를 위한 프래그먼트 클래스.
 */
class AdditionFragment : Fragment() {
    // 프래그먼트 레이아웃 바인딩
    private var _binding: FragmentAdditionBinding? = null
    private val binding get() = _binding!!
    // 트랜잭션 데이터 접근을 위한 리포지토리
    private lateinit var repository: AdditionRepository
    // 저장 전 임시로 트랜잭션을 저장할 리스트
    private val tempTransactionList = mutableListOf<TransactionEntity>()
    // RecyclerView 에 트랜잭션을 표시하기 위한 어댑터
    private lateinit var additionAdapter: AdditionAdapter

    /**
     * 프래그먼트의 뷰를 생성할 때 호출
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdditionBinding.inflate(inflater, container, false)
        repository = AdditionRepository(requireContext())
        return binding.root
    }

    /**
     * 뷰가 생성된 후 호출
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        updateFinishButtonVisibility()
    }

    /**
     * 트랜잭션을 표시할 RecyclerView 를 설정
     */
    private fun setupRecyclerView() {
        additionAdapter = AdditionAdapter { transaction ->
            tempTransactionList.remove(transaction)
            additionAdapter.submitList(tempTransactionList.toList())
            Log.d("AdditionFragment", "Removed from temp list: $transaction")
            updateFinishButtonVisibility()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = additionAdapter
        }
    }

    /**
     * UI 요소에 대한 리스너들을 설정
     */
    private fun setupListeners() {
        binding.fabOpenBottomSheet.setOnClickListener {
            val bottomSheetFragment = RegisterBottomSheetFragment()
            bottomSheetFragment.setOnAddListener { addition ->
                tempTransactionList.add(addition)
                additionAdapter.submitList(tempTransactionList.toList())
                Toast.makeText(context, "추가됨: ${addition.title}", Toast.LENGTH_SHORT).show()
                Log.d("AdditionFragment", "Added to temp list: $addition")
                updateFinishButtonVisibility()
            }
            bottomSheetFragment.show(parentFragmentManager, "register_bottom_sheet")
        }
        binding.btnFinish.setOnClickListener {
            saveAllTransactions()
        }
    }

    /**
     * 임시 리스트에 있는 모든 트랜잭션을 데이터베이스에 저장
     */
    private fun saveAllTransactions() {
        Thread {
            try {
                Log.d("AdditionFragment", "Saving transactions: $tempTransactionList")
                repository.insertAllTransactions(tempTransactionList)
                Log.d("AdditionFragment", "Transactions saved successfully")
                tempTransactionList.clear()
                activity?.runOnUiThread {
                    additionAdapter.submitList(tempTransactionList.toList())
                    Toast.makeText(context, "모든 거래 저장 완료", Toast.LENGTH_SHORT).show()
                    binding.btnFinish.isVisible = false
                }
            } catch (e: Exception) {
                Log.e("AdditionFragment", "Error saving transactions", e)
            }
        }.start()
    }

    /**
     * 임시 트랜잭션 리스트 비움
     */
    private fun clearTempTranscationList() {
        tempTransactionList.clear()
        additionAdapter.submitList(tempTransactionList.toList())
    }

    /**
     * 데이터베이스에서 모든 트랜잭션 로드 후 RecyclerView 에 표시
     */
    private fun loadAllTransactions() {
        Thread {
            val transactions = repository.getAllTransactions()
            activity?.runOnUiThread {
                additionAdapter.submitList(transactions)
                transactions.forEach {
                    Log.d(
                        "Transaction",
                        "ID: ${it.id}, Title: ${it.title}, Amount: ${it.amount}, Date: ${it.transactionDate}, Description: ${it.description}, Category: ${it.categoryName}"
                    )
                }
            }
        }.start()
    }

    /**
     * 임시 트랜잭션 리스트의 상태에 따라 완료 버튼의 가시성 업데이트
     */
    private fun updateFinishButtonVisibility() {
        binding.btnFinish.isVisible = tempTransactionList.isNotEmpty() || additionAdapter.itemCount > 0
    }

    /**
     * 뷰가 파괴될 때 호출
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
