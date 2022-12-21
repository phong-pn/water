package com.phongpn.water.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.gson.Gson
import com.phongpn.water.R
import com.phongpn.water.notification.service.NotificationService
import com.phongpn.water.util.formatTime
import com.phongpn.water.util.profileparams.BaseProfileParamsObserve
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.commons.lang3.text.StrBuilder
import java.lang.StringBuilder
import java.util.*
import kotlin.Comparator

/**
 * Class hold data for schedule notification reminder
 */
@Parcelize
data class AlarmSchedule private constructor(
    var mon: Boolean = true,
    var tue: Boolean = true,
    var wed: Boolean = true,
    var thu: Boolean = true,
    var fri: Boolean = true,
    var sat: Boolean = true,
    var sun: Boolean = true,
    var hourFirst: Int = 8,
    var minuteFirst: Int = 0,
    var hourEnd: Int = 18,
    var minuteEnd: Int = 0,
    var repeatTime: Int = 15,
    var sound: String = SOUND_DEFAULT,
    var vibrate: Boolean = true,
    var active: Boolean = true
) : Parcelable, BaseProfileParamsObserve() {
    private fun createListAlarm() {
        val calFirst = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourFirst)
            set(Calendar.MINUTE, minuteFirst)
            set(Calendar.SECOND, 0)
        }
        val calEnd = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourEnd)
            set(Calendar.MINUTE, minuteEnd)
            set(Calendar.SECOND, 0)
        }
        if (calEnd.timeInMillis < calFirst.timeInMillis) calEnd.roll(Calendar.DAY_OF_MONTH, 1)
        var id = 0
        val now = Calendar.getInstance().timeInMillis
        listAlarm = mutableListOf()
        for (time in calFirst.timeInMillis..calEnd.timeInMillis + 100 step repeatTime * 60*1000L){
            val alarmTime = Calendar.getInstance().apply {
                timeInMillis = time
                set(Calendar.DAY_OF_MONTH, calFirst.get(Calendar.DAY_OF_MONTH))
            }.timeInMillis
            listAlarm += if (alarmTime < now ) Alarm(id++, alarmTime + 24*60*60*1000)
            else Alarm(id++, alarmTime)
        }
    }

    fun schedule(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            createListAlarm()
            listAlarm.forEach { it.schedule(context) }
            save(context)
        }
        Toast.makeText(context,
            context.getString(R.string.notification_was_set) + ": ${formatTime(hourFirst, minuteFirst)} - ${formatTime(hourEnd, minuteEnd)} " +
                    getDisplayRepeat(context),
            Toast.LENGTH_SHORT
            ).show()

    }

    fun cancel(context: Context) {
        listAlarm.forEach { it.cancel(context) }
    }

    fun getNextAlarm(): Alarm? {
        createListAlarm()
        if (listAlarm.size == 0) return null
        val now = Calendar.getInstance().timeInMillis
        listAlarm.sortWith { o1, o2 -> (o1.time - now).compareTo(o2.time - now) }
        return listAlarm[0]
    }

    fun getDisplayRepeat(context: Context): String {
        val display = StringBuilder().apply {
            if (mon) append(context.getString(R.string.mon))
            if (tue) {
                if (length > 0) append(","+context.getString(R.string.tue))
                else append(context.getString(R.string.tue))
            }
            if (wed) {
                if (length > 0) append(","+context.getString(R.string.wed))
                else append(context.getString(R.string.wed))
            }
            if (thu) {
                if (length > 0) append(","+context.getString(R.string.thu))
                else append(context.getString(R.string.thu))
            }
            if (fri) {
                if (length > 0) append(","+context.getString(R.string.fri))
                else append(context.getString(R.string.fri))
            }
            if (sat) {
                if (length > 0) append(","+context.getString(R.string.sat))
                else append(context.getString(R.string.sat))
            }
            if (sun) {
                if (length > 0) append(","+context.getString(R.string.sun))
                else append(context.getString(R.string.sun))
            }

        }
        return display.toString()
    }
    fun copy() = AlarmSchedule(mon, tue, wed, thu, fri, sat, sun, hourFirst, minuteFirst, hourEnd, minuteEnd, repeatTime, sound, vibrate, active)
    companion object {
        private var instance : AlarmSchedule? = null

        fun get() = instance ?: AlarmSchedule()

        /**
         * only use in when Application's onCreate() invoked, or Notification Service launch
         */
        fun get(context: Context) : AlarmSchedule{
            instance = instance ?: context.getSharedPreferences(ALARM, Context.MODE_PRIVATE)
                .getString(ALARM, null)?.let {
                    Gson().fromJson(it, AlarmSchedule::class.java)
                }
            instance = instance ?: AlarmSchedule()
            return instance!!
        }

        fun set(alarmSchedule: AlarmSchedule){
            instance?.apply {
                mon = alarmSchedule.mon
                tue = alarmSchedule.tue
                wed = alarmSchedule.wed
                thu = alarmSchedule.thu
                fri = alarmSchedule.fri
                sat = alarmSchedule.sat
                sun = alarmSchedule.sun
                hourFirst = alarmSchedule.hourFirst
                minuteFirst = alarmSchedule.minuteFirst
                hourEnd = alarmSchedule.hourEnd
                minuteEnd = alarmSchedule.minuteEnd
                repeatTime = alarmSchedule.repeatTime
                sound = alarmSchedule.sound
                vibrate = alarmSchedule.vibrate
                active = alarmSchedule.active
            }
        }

        fun save(context: Context){
            val alarmJson = Gson().toJson(instance)
            context.getSharedPreferences(ALARM, Context.MODE_PRIVATE)
                .edit().apply {
                    putString(ALARM, alarmJson)
                    apply()
                }
        }

        @Volatile
        private var listAlarm = mutableListOf<Alarm>()

        /**
         * Name present this class
         */
        const val ALARM = "alarm"

        const val SOUND_DEFAULT = "Default"

        const val SOUND_WATER = "Water"

        const val SOUND_NONE = "None"

    }
}
