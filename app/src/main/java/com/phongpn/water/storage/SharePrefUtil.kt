package com.phongpn.water.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.phongpn.water.util.profileparams.AppSetting

object SharePrefUtil {
    private lateinit var sharedPreferences: SharedPreferences
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("drink_water", Context.MODE_PRIVATE)
    }

    var appSetting: AppSetting
        get() = Gson().fromJson(sharedPreferences.getString("app_setting", ""), AppSetting::class.java) ?: AppSetting()
        set(value) {
            sharedPreferences.edit().putString("app_setting", Gson().toJson(value))
        }

}