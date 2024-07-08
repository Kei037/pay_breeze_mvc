package com.kei037.pay_breeze_mvc.ui.setting

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kei037.pay_breeze_mvc.databinding.FragmentSettingBinding
import com.kei037.pay_breeze_mvc.databinding.DialogContextBinding
import com.kei037.pay_breeze_mvc.ui.commons.EditCategoryActivity

class SettingFragment : Fragment() {
    // viewBinding 초기화
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private var dX = 0f
    private var dY = 0f
    private var lastAction = 0
    private val developerEmail = "juneway6230@gamilc.com"
    private val appVersion = "v 0.1"

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
            val intent = Intent(activity, EditCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.goHistoricalTrans.setOnClickListener {
            val intent = Intent(activity, HistoricalTransActivity::class.java)
            startActivity(intent)
        }

        binding.contactDeveloper.setOnClickListener {
            showEmailDialog(developerEmail)
        }

        binding.versionTextView.text = appVersion
    }

    private fun showEmailDialog(email: String) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogContextBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.dialogTitle.setOnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = dialog.window!!.decorView.x - event.rawX
                    dY = dialog.window!!.decorView.y - event.rawY
                    lastAction = MotionEvent.ACTION_DOWN
                }
                MotionEvent.ACTION_MOVE -> {
                    dialog.window!!.decorView.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                    lastAction = MotionEvent.ACTION_MOVE
                }
                MotionEvent.ACTION_UP -> {
                    if (lastAction == MotionEvent.ACTION_DOWN)
                        dialog.dismiss()
                }
                else -> return@setOnTouchListener false
            }
            true
        }

        // 이메일 텍스트 설정
        dialogBinding.dialogContent.text = "Email: $email"

        dialogBinding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * 화면 전환시 viewBinding 해제
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
