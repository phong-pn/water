package com.phongpn.water.util

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.constant.params.OZ_UK
import com.phongpn.water.util.profileparams.UnitParams
import java.util.*

fun formatTime(cal : Calendar) = String.format("%02d:%02d", cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE])

fun formatTime(hour : Int, minute: Int) = String.format("%02d:%02d", hour, minute)

fun formatDate(cal: Calendar) = "${String.format("%02d", cal[Calendar.DAY_OF_MONTH])} " +
        "${cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())} " +
        if (cal[Calendar.YEAR] == Calendar.getInstance()[Calendar.YEAR])
        "(${cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())})"
        else "${cal.get(Calendar.YEAR)}"

fun formatDrinkUnit(amount : Int, unit : String) = when(UnitParams.getInstance().unitDrink){
    ML -> "${amount.toMl(unit)} ${UnitParams.getInstance().unitDrink}"
    OZ_UK -> "${amount.toOz_Uk(unit)} ${UnitParams.getInstance().unitDrink}"
    else -> "${amount.toOz_Us(unit)} ${UnitParams.getInstance().unitDrink}"
}

fun Drawable.changeGradient(startColor : Int, endColor : Int, orientation: GradientDrawable.Orientation) {
    (this as GradientDrawable).apply {
        mutate()
        setTintList(null)
        colors = intArrayOf(startColor, endColor)
        this.orientation = orientation
    }
}



