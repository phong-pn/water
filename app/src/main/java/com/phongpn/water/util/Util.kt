package com.phongpn.water.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import java.util.*

fun formatTime(cal: Calendar) =
    String.format("%02d:%02d", cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE])

fun formatTime(hour: Int, minute: Int) = String.format("%02d:%02d", hour, minute)

fun formatDate(cal: Calendar) = "${String.format("%02d", cal[Calendar.DAY_OF_MONTH])} " +
        "${cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())} " +
        if (cal[Calendar.YEAR] == Calendar.getInstance()[Calendar.YEAR])
            "(${cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())})"
        else "${cal.get(Calendar.YEAR)}"

fun Drawable.changeGradient(
    startColor: Int,
    endColor: Int,
    orientation: GradientDrawable.Orientation
) {
    (this as GradientDrawable).apply {
        mutate()
        setTintList(null)
        colors = intArrayOf(startColor, endColor)
        this.orientation = orientation
    }
}



