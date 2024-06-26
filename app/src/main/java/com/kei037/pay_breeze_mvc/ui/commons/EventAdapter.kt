package com.kei037.pay_breeze_mvc.ui.commons

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.DateItem
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.EventItem
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.ListItem
import com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter.ViewType
import com.kei037.pay_breeze_mvc.ui.home.homeAdapter.HomeItem

// RecyclerView의 어댑터 클래스 정의, 여러 종류의 뷰 타입을 처리
class EventAdapter(private var items: List<ListItem>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 이벤트 아이템에 대한 ViewHolder 클래스 정의
    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 각 뷰에 대한 참조 생성
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val categoryTextView: TextView = view.findViewById(R.id.categoryTextView)
        val amountTextView: TextView = view.findViewById(R.id.amountTextView)
    }

    // 날짜 헤더에 대한 ViewHolder 클래스 정의
    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 날짜 텍스트 뷰에 대한 참조 생성
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val categoryTextView: TextView = view.findViewById(R.id.categoryTextView)
        val amountTextView: TextView = view.findViewById(R.id.amountTextView)
    }

    // ViewHolder를 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            // 뷰 타입이 날짜 헤더일 경우 DateViewHolder 생성
            ViewType.DATE_HEADER.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
                DateViewHolder(view)
            }
            // 뷰 타입이 이벤트 아이템일 경우 EventViewHolder 생성
            ViewType.EVENT_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
                EventViewHolder(view)
            }
            ViewType.HOME_ITEM.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
                HomeViewHolder(view)
            }

            // 그 외의 경우 예외 처리
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // ViewHolder에 데이터를 바인딩하는 함수
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            // DateViewHolder일 경우
            is DateViewHolder -> {
                val dateItem = item as DateItem
                holder.dateTextView.text = dateItem.date
            }
            // EventViewHolder일 경우
            is EventViewHolder -> {
                val eventItem = item as EventItem
                holder.titleTextView.text = eventItem.transaction.title
                holder.categoryTextView.text = eventItem.transaction.categoryName
                holder.amountTextView.text = eventItem.transaction.amount.toString()

                // 아이템 클릭 이벤트 처리
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, DetailedActivity::class.java).apply {
                        putExtra("EVENT_DETAIL", eventItem.transaction.toString())
                    }
                    context.startActivity(intent)
                }
            }
            is HomeViewHolder -> {
                val homeItem = item as HomeItem
                holder.titleTextView.text = homeItem.title
                holder.categoryTextView.text = homeItem.categoryName
                holder.amountTextView.text = homeItem.amount.toString()

                // 아이템 클릭 이벤트 처리
                holder.itemView.setOnClickListener {
                    val intent = Intent(context, DetailedActivity::class.java).apply {
                        putExtra("Event_detail", homeItem.transaction.toString())
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    // 아이템의 총 개수를 반환하는 함수
    override fun getItemCount(): Int = items.size

    // 각 아이템의 뷰 타입을 반환하는 함수
    override fun getItemViewType(position: Int): Int {
        return items[position].getType().ordinal
    }

    // 데이터 업데이트 함수
    fun updateEvents(newItems: List<ListItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}