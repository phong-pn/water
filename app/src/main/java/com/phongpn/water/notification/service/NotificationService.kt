package com.phongpn.water.notification.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.phongpn.water.R
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.notification.AlarmSchedule
import com.phongpn.water.storge.LogDrinkRepo
import com.phongpn.water.ui.MainActivity
import com.phongpn.water.util.CHANNEL_ID
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.formatDrinkUnit
import com.phongpn.water.util.profileparams.WaterIntakeParams
import java.util.*

class NotificationService : Service() {
    private lateinit var alarmSchedule : AlarmSchedule

    private lateinit var player : MediaPlayer
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alarmSchedule = AlarmSchedule.get(this).copy()
        val amount = LogDrinkRepo.totalAmountByIdDate(LogDrink(0,"", Calendar.getInstance()).idDate)
        val amountTotalToday = WaterIntakeParams.getInstance().amount
        if (amount < amountTotalToday && haveScheduleToday()){
            intent?.let {
                var notificationBuilder = createNotificationBuilder()
                alarmSchedule.apply {
                    when(sound){
                        AlarmSchedule.SOUND_DEFAULT -> Settings.System.DEFAULT_NOTIFICATION_URI
                        AlarmSchedule.SOUND_WATER -> Uri.parse("android.resource://" +
                                this@NotificationService.packageName + "/"+"${R.raw.water_effect}")
                        else -> null
                    }?.let {
                        player  = MediaPlayer()
                        try {
                            player.apply {
                                setDataSource(this@NotificationService, it)
                                prepare()
                                start()
                            }
                        }
                        catch (e : Exception){
                            e.printStackTrace()
                        }
                    }

                    if (vibrate) {
                        val vibrateAttributes = AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator.vibrate(
                                VibrationEffect.createOneShot(500, -1), vibrateAttributes)
                        }
                        else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                                    VibrationEffect.createOneShot(500, -1), vibrateAttributes)
                            }
                            else{
                                (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(500)
                            }
                        }
                    }
                    notificationBuilder.apply {
                        setContentText(getString(R.string.you_need_to_get) + ": ${formatDrinkUnit(amountTotalToday - amount, ML)}")
                    }
                    NotificationManagerCompat.from(this@NotificationService).notify(
                        1,
                        notificationBuilder.build()
                    )
                }
            }
        }
        return START_NOT_STICKY
    }
    private fun createNotificationBuilder() : NotificationCompat.Builder{
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val icon = IconCompat.createWithResource(this, R.drawable.notification_icon)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(getString(R.string.reminder_water))
            .setAutoCancel(true)
            .setColor(getColor(R.color.blue))
            .setColorized(true)
            .setShortcutId(CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    private fun haveScheduleToday() : Boolean{
        return when(Calendar.getInstance()[Calendar.DAY_OF_WEEK]){
            Calendar.MONDAY -> alarmSchedule.mon
            Calendar.TUESDAY -> alarmSchedule.tue
            Calendar.WEDNESDAY -> alarmSchedule.wed
            Calendar.THURSDAY -> alarmSchedule.thu
            Calendar.FRIDAY -> alarmSchedule.fri
            Calendar.SATURDAY -> alarmSchedule.sat
            else-> alarmSchedule.sun
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::player.isInitialized) player.release()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}