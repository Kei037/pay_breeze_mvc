package com.kei037.pay_breeze_mvc.ui.addition

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.data.repository.CategoryRepository
import com.kei037.pay_breeze_mvc.databinding.RegisterBottomSheetBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * bottom sheet fragment : 새로운 트랜잭션을 등록
 */
class RegisterBottomSheetFragment : BottomSheetDialogFragment() {

    // fragment layout binding
    private var _binding: RegisterBottomSheetBinding? = null
    private val binding get() = _binding!!
    // 기본 선택 category
    private var selectedCategory: String = "Food"
    // 선택한 transaction 날짜
    private var selectedDate: Date? = null
    // Transaction 지출 여부 flag
    private var isExpense: Boolean = true
    // Transaction 추가 리스너
    private var onAddListener: ((TransactionEntity) -> Unit)? = null
    // UI 상태 관리를 위한 flag
    private var isCreatingNewCategory = false
    private var isEditingCategories = false
    // UI 구성 요소
    private lateinit var createNewChip: Chip
    private lateinit var cancelChip: Chip
    private lateinit var repository: AdditionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var tvEdit: TextView
    private val tempTransactionList = mutableListOf<TransactionEntity>()

    /**
     * Fragment view 생성 및 반환
     *
     * @param inflater : layout inflater
     * @param container 부모 view group
     * @param savedInstanceState : 저장된 인스턴스 상태
     * @return 생성된 view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = RegisterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * dialog 생성 및 반환
     *
     * @param savedInstanceState 저장된 인스턴스 상태
     * @return 생성된 dialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    /**
     * view 생성 후 호출
     *
     * @param view : 생성된 view
     * @param savedInstanceState : 저장된 인스턴스 상태
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // repository 초기화
        categoryRepository = CategoryRepository(requireContext())
        repository = AdditionRepository(requireContext())
        createNewChip = binding.categoryCreateNew
        cancelChip = binding.categoryCancel
        tvEdit = binding.tvEdit

        // listener 설정
        setupListeners()
        // category 설정
        loadCategories()

        // dialog 가 보여질 때 동작 설정
        dialog?.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val layoutParams = it.layoutParams as ViewGroup.LayoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                it.layoutParams = layoutParams
                val behavior = BottomSheetBehavior.from(it)

                val itemView =
                    LayoutInflater.from(context).inflate(R.layout.item_addition, it, false)
                itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                val listItemHeight = itemView.measuredHeight
                val screenHeight = resources.displayMetrics.heightPixels
                behavior.peekHeight = screenHeight - listItemHeight
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED

                it.setBackgroundResource(R.drawable.bg_register_bottom_sheet)
            }
        }
    }

    /**
     * 多 listener 설정
     */
    private fun setupListeners() {
        // category chip 그룹의 선택 상태가 변경될 때 호출 되는 리스너 설정
        binding.chipGroupCategory.setOnCheckedStateChangeListener { group, checkedIds ->
            // 모든 Chip 아이콘 비활성화
            group.children.forEach { chip ->
                (chip as Chip).chipIcon = null
                chip.isChipIconVisible = false
            }
            // 선택된 Chip 이 있을 경우, 해당 Chip 의 아이콘 활성화
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds[0])
                if (chip != null) {
                    selectedCategory = chip.text.toString()
                    chip.chipIcon = context?.getDrawable(R.drawable.checked_chip_icon)
                    chip.isChipIconVisible = true
                    Log.d("BottomSheet", "Selected category: $selectedCategory")
                    chip.invalidate()  // UI 강제 업데이트
                }
            }
        }

        // 날짜 입력 필드 클릭 시 날짜 선택 dialog 표시
        binding.dateInput.setOnClickListener {
            showDatePicker()
        }

        // 수입 Chip 클릭 시 동작 설정
        binding.chipIncome.setOnClickListener {
            selectIncomeExpenseChip(true)
        }

        // 지출 Chip 클릭 시 동작 설정
        binding.chipExpense.setOnClickListener {
            selectIncomeExpenseChip(false)
        }

        // 추가 버튼 클릭 시 새 transaction 추가
        binding.btnAdd.setOnClickListener {
            addAddition()
        }

        // create new 버튼 클릭 시 동작 설정 :: custom category 생성 버튼
        binding.categoryCreateNew.setOnClickListener {
            if (!isCreatingNewCategory) {
                addEditableChip()
                createNewChip.text = "confirm"
            } else {
                val lastChip =
                    binding.chipGroupCategory.getChildAt(binding.chipGroupCategory.childCount - 2)
                if (lastChip is CustomChip) {
                    saveNewCategory(lastChip)
                } else {
                    Toast.makeText(context, "Failed to create new category", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // cancel 버튼 클릭 시 동작 설정 :: category 생성 취소 버튼
        binding.categoryCancel.setOnClickListener {
            cancelNewCategoryCreation()
        }

        // edit 버튼 클릭 시 동작 설정 :: custom category edit 버튼
        tvEdit.setOnClickListener {
            toggleEditMode()
        }
    }

    /**
     * 수정 가능한 Chip 추가
     */
    private fun addEditableChip() {
        val chipGroup = binding.chipGroupCategory
        val customChip = CustomChip(context)
        customChip.setOnDoneListener {
            saveNewCategory(customChip)
        }
        chipGroup.addView(customChip, chipGroup.childCount - 1)
        customChip.focusEditText()
        createNewChip.setChipIconResource(R.drawable.checked_chip_icon)
        isCreatingNewCategory = true
        binding.categoryCancel.visibility = View.VISIBLE
        Log.d("BottomSheet", "Editable chip added")
    }

    /**
     * 새 category 를 database 에 저장
     * @param customChip : 입력된 새 category 이름을 가진 custom Chip
     */
    private fun saveNewCategory(customChip: CustomChip) {
        val categoryName = customChip.getText()
        if (categoryName.isNotEmpty()) {
            SaveCategoryTask(categoryRepository, categoryName, customChip).execute()
        } else {
            Toast.makeText(context, "Category name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * AsyncTask : 비동기적으로 category 를 database 에 저장
     */
    private inner class SaveCategoryTask(
        private val repository: CategoryRepository,
        private val categoryName: String,
        private val customChip: CustomChip
    ) : AsyncTask<Void, Void, Boolean>() {
        /**
         * category 를 database 에 저장하는 작업을 background 에서 수행
         * @param params : 작업에 사용되지 않는 parameter
         * @return Boolean : 저장 성공 여부
         */
        override fun doInBackground(vararg params: Void?): Boolean {
            return try {
                val newCategory = CategoryEntity(name = categoryName, isPublic = true)
                repository.insertCategory(newCategory)
                Log.d("BottomSheet", "Category saved to DB: $categoryName")
                true
            } catch (e: Exception) {
                Log.e("BottomSheet", "Failed to save category", e)
                false
            }
        }

        /**
         * background 작업 후 UI 업데이트
         * @param result : 저장 성공 여부
         */
        override fun onPostExecute(result: Boolean) {
            if (result) {
                val chipGroup = binding.chipGroupCategory
                val chip = Chip(context)
                chip.text = categoryName
                chip.isCheckable = true
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    val chipButton = buttonView as Chip
                    chipButton.chipIcon = if (isChecked) context?.getDrawable(R.drawable.checked_chip_icon) else null
                }
                chipGroup.addView(chip, chipGroup.childCount - 2)
                chipGroup.removeView(customChip)
                createNewChip.setChipIconResource(R.drawable.nav_addition_icon)
                createNewChip.text = "Create New"
                isCreatingNewCategory = false
                binding.categoryCancel.visibility = View.GONE
                Log.d("BottomSheet", "Category saved and UI updated")
            } else {
                Toast.makeText(context, "Failed to create a new category", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * 임시 리스트에 새 transaction 추가
     * @param categoryName : transaction 의 category 이름
     */
    private fun addCategoryToTempList(categoryName: String) {
        val addition = TransactionEntity(
            title = "Temp Title",
            amount = 0.0,
            transactionDate = Date().toString(),
            description = "Temp Description",
            categoryName = categoryName
        )
        tempTransactionList.add(addition)
        Log.d("BottomSheet", "Added to temp list: $addition")
    }

    /**
     * 새 category 생성 취소
     */
    private fun cancelNewCategoryCreation() {
        val chipGroup = binding.chipGroupCategory
        val lastChip = chipGroup.getChildAt(chipGroup.childCount - 2)
        if (lastChip is CustomChip) {
            chipGroup.removeView(lastChip)
            createNewChip.setChipIconResource(R.drawable.nav_addition_icon)
            createNewChip.text = "Create New"
            isCreatingNewCategory = false
            binding.categoryCancel.visibility = View.GONE
            Log.d("BottomSheet", "Category creation canceled")
        }
    }

    /**
     * 수입/지출 선택 Chip 업데이트
     * @param isIncome 수입 여부
     */
    private fun selectIncomeExpenseChip(isIncome: Boolean) {
        isExpense = !isIncome

        val incomeChip = binding.chipIncome
        val expenseChip = binding.chipExpense

        if (isIncome) {
            incomeChip.apply {
                isChecked = true
                setChipBackgroundColorResource(R.color.black)
                setTextColor(Color.WHITE)
            }
            expenseChip.apply {
                isChecked = false
                setChipBackgroundColorResource(R.color.white)
                setTextColor(Color.BLACK)
            }
        } else {
            expenseChip.apply {
                isChecked = true
                setChipBackgroundColorResource(R.color.black)
                setTextColor(Color.WHITE)
            }
            incomeChip.apply {
                isChecked = false
                setChipBackgroundColorResource(R.color.white)
                setTextColor(Color.BLACK)
            }
        }
    }

    /**
     * 날짜 dialog 표시
     */
    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(parentFragmentManager, "date_picker")
        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = Date(it)
            binding.dateInput.setText(sdf.format(selectedDate!!))
            Log.d("BottomSheet", "Selected date: ${sdf.format(selectedDate!!)}")
        }
    }

    /**
     * 새 transaction 추가
     */
    private fun addAddition() {
        val title = binding.etTitle.text.toString()
        val amountText = binding.etAmount.text.toString()
        val amount = amountText.toDoubleOrNull()
        val description = binding.etDescription.text.toString()

        if (title.isEmpty() || amount == null || description.isEmpty() || selectedDate == null) {
            Toast.makeText(context, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val addition = TransactionEntity(
            title = title,
            amount = if (isExpense) {
                -amount
            } else amount,
            transactionDate = selectedDate!!.toString(),
            description = description,
            categoryName = selectedCategory,
        )

        onAddListener?.invoke(addition)
        clearForm()
        dismiss()
    }

    /**
     * transaction 추가 listener 설정
     * @param listener : 추가된 transaction 처리 listener
     */
    fun setOnAddListener(listener: (TransactionEntity) -> Unit) {
        onAddListener = listener
    }

    /**
     * 입력 폼 초기화
     */
    private fun clearForm() {
        binding.etTitle.text?.clear()
        binding.etAmount.text?.clear()
        binding.etDescription.text?.clear()
        binding.dateInput.text?.clear()
        binding.chipGroupCategory.clearCheck()
        selectedDate = null
        selectedCategory = "Food"
        binding.etAmount.setTextColor(Color.BLACK)
        isExpense = true
    }

    /**
     * database 에서 모든 category 로드
     */
    private fun loadCategories() {
        AsyncTask.execute {
            val category = categoryRepository.getAllCategories()
            activity?.runOnUiThread {
                val chipGroup = binding.chipGroupCategory
                category.forEach { category ->
                    val chip = Chip(context)
                    chip.text = category.name
                    chip.isCheckable = true
                    chip.chipIcon = context?.getDrawable(R.drawable.checked_chip_icon)
                    chip.isChipIconVisible = false
                    chip.setOnCheckedChangeListener { buttonView, isChecked ->
                        val chipButton = buttonView as Chip
                        chipButton.chipIcon = if (isChecked) context?.getDrawable(R.drawable.checked_chip_icon) else null
                        chipButton.isChipIconVisible = isChecked
                    }
                    chip.setOnClickListener {
                        addCategoryToTempList(category.name)
                    }
                    chipGroup.addView(chip)
                }
            }
        }
    }

    /**
     * category 편집 모드 토글
     */
    private fun toggleEditMode() {
        isEditingCategories = !isEditingCategories
        val chipGroup = binding.chipGroupCategory
        val childCount = chipGroup.childCount
        for (i in 0 until childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (!isBuiltInCategory(chip.text.toString())) {
                chip.isCloseIconVisible = isEditingCategories
                if (isEditingCategories) {
                    chip.setOnCloseIconClickListener {
                        deleteCategory(chip.text.toString())
                        chipGroup.removeView(chip)
                    }
                } else {
                    chip.setOnCloseIconClickListener(null)
                }
            }
        }
        tvEdit.text = if (isEditingCategories) "finish" else "edit"
    }

    /**
     * 기본 제공 category 인지 확인
     * @param categoryName : category 이름
     * @return Boolean : 기본 제공 category 여부
     */
    private fun isBuiltInCategory(categoryName: String): Boolean {
        val builtInCategories = listOf("Food", "Fuel", "Shopping", "Mobile Plan", "Rent", "Investment", "Salary")
        return builtInCategories.contains(categoryName)
    }

    /**
     * category 를 database 에서 삭제
     * @param categoryName : 삭제할 category 이름
     */
    private fun deleteCategory(categoryName: String) {
        AsyncTask.execute {
            val category = categoryRepository.getCategoryByName(categoryName)
            if (category.isNotEmpty()) {
                categoryRepository.deleteCategory(category[0])
                Log.d("BottomSheet", "Category deleted from DB: $categoryName")
            }
        }
    }

    /**
     * view 가 파괴될 때 호출
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
