package com.kei037.pay_breeze_mvc.ui.calender.dacorators

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class StartEndDateDecorator (
    private val date: CalendarDay?,
    private val context: Context,
    private val drawableId: Int
) : DayViewDecorator {
    private val drawable: Drawable = ContextCompat.getDrawable(context, drawableId)!!

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable)
    }
}