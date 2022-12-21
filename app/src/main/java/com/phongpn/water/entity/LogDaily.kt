package com.phongpn.water.entity

import android.text.format.DateUtils
import java.util.*

data class LogDaily(
    var idDate: String?,
    val cal : Calendar?,
    var total : Int?
){
    fun getDisplayDate(): String {
        val calendarTemp = Calendar.getInstance().apply {
            timeInMillis -= 7 * 86400 * 1000
        }
        val isInWeek = cal!!.timeInMillis > calendarTemp.timeInMillis && cal.timeInMillis < Calendar.getInstance().timeInMillis
        return if (isInWeek) cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())!!
        else "${cal[Calendar.DAY_OF_MONTH]} ${cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())}"
    }
}