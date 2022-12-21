package com.phongpn.water.util.profileparams

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Modifier

class AppSetting : BaseProfileParamsObserve(), SaveToSharePreference {
    companion object {
        const val THEME = "theme"
        const val SOUND = "sound"
        const val FIRST_LAUNCH = "first_launch"
        const val WATER_THEME = "water"
        private const val APP_SETTING = "AppSetting"

        private var instance = AppSetting()
        fun getInstance() = instance
    }

    var sound = true
        set(value) {
            field = value
            notifyDataChanged(SOUND, field)
        }
    var theme = WATER_THEME
        set(value) {
            field = value
            notifyDataChanged(THEME, field)
        }
    var firstLaunch = true
    var launch = 0

    override fun get(context: Context) {
        context.getSharedPreferences(APP_SETTING, Context.MODE_PRIVATE)
            .getString(APP_SETTING, null)
            ?.let {
                instance = Gson().fromJson(it, AppSetting::class.java)
            }
    }
    override fun save(context: Context) {
        Gson().toJson(instance).also { json ->
            context.getSharedPreferences(APP_SETTING, Context.MODE_PRIVATE).edit {
                putString(APP_SETTING, json)
            }
        }
    }

}