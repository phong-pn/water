package com.phongpn.water.storge

import androidx.room.TypeConverter
import java.util.*

class CalendarConvert {
    @TypeConverter
    fun longToCalendar(cal : Long) : Calendar{
        return Calendar.getInstance().apply {
            time = Date(cal)
        }
    }
    @TypeConverter
    fun calendarToLong(calendar: Calendar) = calendar.time.time
}