package com.kei037.pay_breeze_mvc.ui.calender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kei037.pay_breeze_mvc.R
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity

class EventAdapter(private var events: List<TransactionEntity>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val categoryTextView: TextView = view.findViewById(R.id.categoryTextView)
        val amountTextView: TextView = view.findViewById(R.id.amountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.titleTextView.text = event.title
        holder.categoryTextView.text = event.categoryName
        holder.amountTextView.text = event.amount.toString()
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun updateEvents(newEvents: List<TransactionEntity>) {
        events = newEvents
        notifyDataSetChanged()
    }
}