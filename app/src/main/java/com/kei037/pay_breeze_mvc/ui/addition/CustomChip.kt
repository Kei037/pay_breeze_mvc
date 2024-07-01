package com.kei037.pay_breeze_mvc.ui.addition

import android.content.Context
import android.graphics.Color
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import com.kei037.pay_breeze_mvc.R

/**
 * CustomChip : 사용자가 새 category 를 입력하는 Customizing Chip
 *
 * @constructor : Chip 생성 Context
 */
class CustomChip(context: Context?) : LinearLayout(context) {

    // 카테고리 이름을 입력하는 EditText
    private val editText: EditText = EditText(context)
    // 완료 버튼 클릭 리스너
    private var onDoneListener: (() -> Unit)? = null

    init {
        orientation = HORIZONTAL
        // Chip 생성 및 설정
        val chip = Chip(context)
        chip.text = ""
        chip.isClickable = false
        chip.isFocusable = false
//        chip.chipIcon = context?.getDrawable(R.drawable.checked_chip_icon)
        chip.setChipBackgroundColorResource(android.R.color.transparent)

        // EditText 설정
        editText.hint = "New Category"
        editText.setTextColor(Color.BLACK)
        editText.background = null
        editText.setSingleLine(true)
        editText.imeOptions = EditorInfo.IME_ACTION_DONE
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onDoneListener?.invoke()
                true
            } else {
                false
            }
        }

        // layout 에 chip 추가
        addView(chip)
        // layout 에 editText 추가
        addView(editText)
    }

    /**
     * EditText 에 포커스 설정 후 키보드 표시
     */
    fun focusEditText() {
        editText.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * EditText 에 입력된 텍스트 반환
     *
     * @return 입력된 텍스트
     */
    fun getText(): String {
        return editText.text.toString()
    }

    /**
     * EditText 힌트 텍스트 설정
     *
     * @param hint : 힌트 텍스트
     */
    fun setHintText(hint: String) {
        editText.hint = hint
    }

    /**
     * 완료 버튼 클릭 리스너 설정
     *
     * @param listener : 완료 버튼 클릭 리스너
     */
    fun setOnDoneListener(listener: () -> Unit) {
        onDoneListener = listener
    }
}
