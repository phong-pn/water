package com.phongpn.water.storage

import android.content.Context
import com.phongpn.water.notification.AlarmSchedule

object Pref {
    fun save(context: Context) {
        context.apply {
            AlarmSchedule.save()
        }
    }

}