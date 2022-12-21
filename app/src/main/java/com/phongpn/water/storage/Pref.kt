package com.phongpn.water.storage

import android.content.Context
import com.phongpn.water.notification.AlarmSchedule
import com.phongpn.water.util.profileparams.AppSetting
import com.phongpn.water.util.profileparams.UnitParams
import com.phongpn.water.util.profileparams.WaterIntakeParams

object Pref {
    fun save(context: Context){
        context.apply {
            AppSetting.getInstance().save(this)
            UnitParams.getInstance().save(this)
            AlarmSchedule.save(this)
            WaterIntakeParams.getInstance().save(this)
        }
    }

    fun getData(context: Context){
        context.apply {
            UnitParams.getInstance().get(this)
            AppSetting.getInstance().get(this)
            WaterIntakeParams.getInstance().get(this)
            AlarmSchedule.get(this)
        }
    }
}