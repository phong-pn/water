package com.phongpn.water.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReScheduleBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && context != null){
            val alarmSchedule = AlarmSchedule.get(context)
            alarmSchedule.schedule(context)
        }
    }
}