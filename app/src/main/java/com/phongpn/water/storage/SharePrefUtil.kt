package com.phongpn.water.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.phongpn.water.notification.AlarmSchedule
import com.phongpn.water.util.constant.params.KG
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.profileparams.WaterIntakeParams

object SharePrefUtil {
    private lateinit var sharedPreferences: SharedPreferences
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("drink_water", Context.MODE_PRIVATE)
    }

    var firstLaunch: Boolean
        get() = sharedPreferences.getBoolean("first_launch", true)
        set(value) {
            sharedPreferences.edit().putBoolean("first_launch", value).apply()
        }

    var sound: Boolean
        get() = sharedPreferences.getBoolean("sound", true)
        set(value) {
            sharedPreferences.edit().putBoolean("app_setting", value).apply()
        }

    var unitDrink: String
        get() = sharedPreferences.getString("unit_drink", ML)!!
        set(value) {
            sharedPreferences.edit().putString("unit_drink", value).apply()
        }

    var unitWeight: String
        get() = sharedPreferences.getString("unit_weight", KG)!!
        set(value) {
            sharedPreferences.edit().putString("unit_weight", value).apply()
        }

    var sex: String
        get() = sharedPreferences.getString("sex", WaterIntakeParams.MALE)!!
        set(value) {
            sharedPreferences.edit().putString("sex", value).apply()
        }

    var pregnant: Boolean
        get() = sharedPreferences.getBoolean("pregnant", false)
        set(value) {
            sharedPreferences.edit().putBoolean("pregnant", value).apply()
        }

    var breastfeeding: Boolean
        get() = sharedPreferences.getBoolean("breastfeeding", false)
        set(value) {
            sharedPreferences.edit().putBoolean("breastfeeding", value).apply()
        }

    var weight: Int
        get() = sharedPreferences.getInt("weight", 60)
        set(value) {
            sharedPreferences.edit().putInt("weight", value).apply()
        }

    var activity: String
        get() = sharedPreferences.getString("activity", WaterIntakeParams.ACTIVE)!!
        set(value) {
            sharedPreferences.edit().putString("activity", value).apply()
        }

    var weather: String
        get() = sharedPreferences.getString("weather", WaterIntakeParams.TEMPERATE)!!
        set(value) {
            sharedPreferences.edit().putString("weather", value).apply()
        }

    var goal: Int
        get() = sharedPreferences.getInt("goal", 2000)
        set(value) {
            sharedPreferences.edit().putInt("goal", value).apply()
        }

    var alarmSchedule: AlarmSchedule
        get() {
            val json = sharedPreferences.getString("alarm", "")
            return if (!json.isNullOrBlank()) Gson().fromJson(json, AlarmSchedule::class.java)
            else AlarmSchedule()
        }
        set(value) {
            val alarmJson = Gson().toJson(AlarmSchedule.get())
            sharedPreferences.edit().putString("alarm", alarmJson).apply()
        }
}