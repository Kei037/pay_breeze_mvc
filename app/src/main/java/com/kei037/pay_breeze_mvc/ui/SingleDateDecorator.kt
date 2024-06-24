package com.kei037.pay_breeze_mvc.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class SingleDateDecorator(private val date: CalendarDay, context: Context, drawableResId: Int) :
    DayViewDecorator {

    private val drawable: Drawable = ContextCompat.getDrawable(context, drawableResId)!!

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable)
    }
}