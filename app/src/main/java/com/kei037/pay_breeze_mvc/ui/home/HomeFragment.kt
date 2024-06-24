package com.kei037.pay_breeze_mvc.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.kei037.pay_breeze_mvc.R

class HomeFragment : Fragment() {

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var twoCl: ConstraintLayout // 투명도가 조절될 ConstraintLayout
    private lateinit var buttonLayout: LinearLayout
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        appBarLayout = view.findViewById(R.id.appBarLayout)
        twoCl = view.findViewById(R.id.two_cl)
        buttonLayout = view.findViewById(R.id.buttonLayout)
        eventRecyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 투명도 조절 및 스크롤
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val alpha = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()
            twoCl.alpha = alpha

            // 투명도가 1일때 버튼레이웃의 marginTop 40으로 변경
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            if (alpha.toInt() == 1) {
                params.setMargins(0, 36, 0, 0)
                buttonLayout.layoutParams = params
            }

            if (alpha.toInt() == 0) {
                params.setMargins(0, 0, 0, 0)
                buttonLayout.layoutParams = params
            }

        })

        // 리사이클러뷰와 어댑터 설정
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = EventAdapter(emptyList()) // 초기에는 빈 리스트로 설정
        eventRecyclerView.adapter = eventAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
