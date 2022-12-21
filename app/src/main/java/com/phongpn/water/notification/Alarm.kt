package com.phongpn.water.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import com.phongpn.water.notification.service.NotificationService
import java.util.*

class Alarm(var id: Int, var time: Long) {
    fun schedule(context: Context){
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time  , prepareAlarmPendingIntent(context))
    }

    fun cancel(context: Context){
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(prepareAlarmPendingIntent(context))
    }

    private fun prepareAlarmIntent(context: Context) = Intent(context, NotificationService::class.java)
    private fun prepareAlarmPendingIntent(context: Context) = PendingIntent.getService(
        context,
        id,
        prepareAlarmIntent(context),
       PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
}