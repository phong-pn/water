package com.phongpn.water

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.phongpn.water.storage.LogDrinkRepo
import com.phongpn.water.storage.Pref
import com.phongpn.water.storage.SharePrefUtil
import com.phongpn.water.util.CHANNEL_ID

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        SharePrefUtil.init(this)
        LogDrinkRepo.initDao(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel()  {
        val channel: NotificationChannel?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(CHANNEL_ID, "Nhắc nhở", NotificationManager.IMPORTANCE_DEFAULT).apply {
                setSound(null, null)
            }
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }
}