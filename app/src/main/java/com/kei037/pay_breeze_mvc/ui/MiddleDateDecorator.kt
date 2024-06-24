package com.kei037.pay_breeze_mvc.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class MiddleDateDecorator(
    private val startDate: CalendarDay?,
    private val endDate: CalendarDay?,
    private val context: Context,
    private val drawableId: Int
) : DayViewDecorator {

    private val drawable: Drawable = ContextCompat.getDrawable(context, drawableId)!!

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return startDate != null && endDate != null && day.isAfter(startDate) && day.isBefore(endDate)
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable)
    }
}

private fun CalendarDay.isAfter(other: CalendarDay): Boolean {
    return this.date.isAfter(other.date)
}

private fun CalendarDay.isBefore(other: CalendarDay): Boolean {
    return this.date.isBefore(other.date)
}