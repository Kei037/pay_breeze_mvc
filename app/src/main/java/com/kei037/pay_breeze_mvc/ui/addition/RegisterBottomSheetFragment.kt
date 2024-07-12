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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.datepicker.MaterialDatePicker
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.AppDatabase
import com.kei037.pay_breeze_mvc.data.db.entity.CategoryEntity
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.data.repository.CategoryRepository
import com.kei037.pay_breeze_mvc.databinding.RegisterBottomSheetBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.CountDownLatch

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

        // 오늘 날짜 설정
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        val formattedDate = sdf.format(currentDate)
        binding.dateInput.setText(formattedDate)

        // 오늘 날짜를 selectedDate에 설정
        selectedDate = currentDate
    }

    /**
     * 多 listener 설정
     * TODO :: 중복되는 category 추가 불가능 로직 추가 필요
     *
     */
    private fun setupListeners() {
        // category chip 그룹의 선택 상태가 변경될 때 호출 되는 리스너 설정
        binding.chipGroupCategory.setOnCheckedStateChangeListener { group, checkedIds ->
            // 모든 Chip 아이콘 비활성화
            group.children.forEach { chip ->
                if (chip is Chip) {
                    chip.chipIcon = null
                    chip.isChipIconVisible = false
                }
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
                val lastChip = binding.chipGroupCategory.getChildAt(binding.chipGroupCategory.childCount - 2)
                if (lastChip is CustomChip) {
                    val categoryName = lastChip.getText()
                    lifecycleScope.launch {
                        if (isCheck(categoryName)) {
                            saveNewCategory(lastChip)
                        } else {
                            Toast.makeText(context, "중복된 카테고리가 존재합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to create new category", Toast.LENGTH_SHORT).show()
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
            lifecycleScope.launch {
                if (isCheck(customChip.getText())) {
                    saveNewCategory(customChip)
                } else {
                    Toast.makeText(context, "Category already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }
        chipGroup.addView(customChip, chipGroup.childCount - 1)
        customChip.focusEditText()
        createNewChip.setChipIconResource(R.drawable.checked_chip_icon)
        isCreatingNewCategory = true
        binding.categoryCancel.visibility = View.VISIBLE
        Log.d("BottomSheet", "Editable chip added")
    }

    /**
     * 카테고리 이름이 중복되지 않는지 체크
     */
    private suspend fun isCheck(categoryName: String): Boolean {
        return withContext(Dispatchers.IO) {
            val db = AppDatabase.getInstance(requireContext())
            val categoryDao = db.getCategoryDao()
            categoryDao.getOneCategoryByName(categoryName) == null
        }
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
         *
         * TODO :: 추가할 때 true -> false 로 변경 (관련 로직 수정)ㅂㅈㄷ
         */
        override fun doInBackground(vararg params: Void?): Boolean {
            return try {
                val newCategory = CategoryEntity(name = categoryName, isPublic = false) //
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
                setChipBackgroundColorResource(R.color.customGreen)
                setTextColor(Color.WHITE)
            }
            expenseChip.apply {
                isChecked = false
                setChipBackgroundColorResource(R.color.customIvory)
                setTextColor(Color.BLACK)
            }
        } else {
            expenseChip.apply {
                isChecked = true
                setChipBackgroundColorResource(R.color.customGreen)
                setTextColor(Color.WHITE)
            }
            incomeChip.apply {
                isChecked = false
                setChipBackgroundColorResource(R.color.customIvory)
                setTextColor(Color.BLACK)
            }
        }
    }

    /**
     * 날짜 dialog 표시
     * Date Picker 표시 및 선택된 날짜를 yyyy-MM-dd 형식 문자열로 formatting 후 UI에 설정 후 로그에 출력
     *
     * TODO :: bottom sheet 로 부터 처음 UI 표시될 때 오늘 날짜 기본값 설정 필요
     *      :: material 3 docked date picker UI 적용 필요
     */
    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.show(parentFragmentManager, "date_picker")
        datePicker.addOnPositiveButtonClickListener {
            // SimpleDateFormat 를 통해 선택된 날짜를 yyyy-MM-dd 형식의 문자열로 formatting
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = Date(it)
            val formattedDate = sdf.format(selectedDate!!)
            // formatting 된 날짜를 dateInput 필드에 설정
            binding.dateInput.setText(formattedDate)
            // Log 에 선택된 날짜 출력
            Log.d("BottomSheet", "Selected date: $formattedDate")
        }
    }

    /**
     * 새 transaction 추가
     *
     * 입력된 데이터 사용하여 TransactionEntity 객체 생성
     * onAddListener 를 통해 전달
     * 날짜를 yyyy-MM-dd 형식의 문자열로 formatting 후 로그 출력
     */
    private fun addAddition() {
        // 입력된 제목, 금액 및 설명 가져오기
        val title = binding.etTitle.text.toString()
        val amountText = binding.etAmount.text.toString()
        val amount = amountText.toDoubleOrNull()
        val description = binding.etDescription.text.toString()

        // 필수 필드 비어있는지 확인
        if (title.isEmpty() || amount == null || description.isEmpty() || selectedDate == null) {
            Toast.makeText(context, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 날짜를 yyyy-MM-dd 형식의 문자열로 formatting
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = sdf.format(selectedDate!!)
        // 로그에 포맷팅된 날짜 출력
        Log.d("AdditionFragment", "Formatted date to be saved: $formattedDate")

        // TransactionEntity 객체 생성
        val addition = TransactionEntity(
            title = title,
            amount = if (isExpense) {
                -amount
            } else amount,
            transactionDate = formattedDate,
            description = description,
            categoryName = selectedCategory,
        )

        // 로그에 생성된 transaction 출력
        Log.d("AdditionFragment", "Transaction to be added: $addition")

        // onAddListener 를 통해 transaction 전달
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
            val categories = categoryRepository.getAllCategories()
            activity?.runOnUiThread {
                setupCategoryChips(categories)
            }
        }
    }

    private fun setupCategoryChips(categories: List<CategoryEntity>) {
        val chipGroup = binding.chipGroupCategory
        categories.forEach { category ->
            val chip = createCategoryChip(category)
            chipGroup.addView(chip)
        }
    }

    private fun createCategoryChip(category: CategoryEntity): Chip {
        val chip = Chip(context).apply {
            text = category.name
            isCheckable = true
            val chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, R.style.CustomChipStyle)
            setChipDrawable(chipDrawable)
            setTextAppearance(R.style.CustomChipStyle) // 스타일 적용
            setOnCheckedChangeListener { buttonView, isChecked ->
                val chipButton = buttonView as Chip
                chipButton.chipIcon = if (isChecked) context?.getDrawable(R.drawable.checked_chip_icon) else null
                chipButton.isChipIconVisible = isChecked
            }
            setOnClickListener {
                addCategoryToTempList(category.name)
            }
        }
        return chip
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
        val latch = CountDownLatch(1)
        var result = false

        Thread {
            val categories: List<CategoryEntity> = categoryRepository.getCategoryByIsPublic(true)
            val builtInCategories: List<String> = categories.map { it.name }
            result = builtInCategories.contains(categoryName)
            latch.countDown() // 작업 완료를 알림
        }.start()

        latch.await() // 스레드가 작업을 완료할 때까지 대기

        return result
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
