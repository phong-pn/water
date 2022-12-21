package com.phongpn.water.ui.profile

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.provider.Settings
import android.view.*
import androidx.core.app.NotificationManagerCompat
import com.phongpn.water.R
import com.phongpn.water.notification.AlarmSchedule
import com.phongpn.water.ui.dialog.*
import com.phongpn.water.ui.dialog.base.BaseBottomSheetDialogFragment
import com.phongpn.water.ui.dialog.base.BaseBodyDialog
import com.phongpn.water.ui.greetings.changeColorCheckBox
import com.phongpn.water.util.formatTime
import com.phongpn.water.util.profileparams.AppSetting
import kotlinx.android.synthetic.main.notification_fragment.*
import java.util.*

class NotificationFragment(title: String) : BaseDetailProfileFragment(title) {
    constructor() : this("")
    val alarm  = AlarmSchedule.get().copy()
    private lateinit var player : MediaPlayer
    private var isAlarmChanged = false
        set(value) {
            field = value
            if (value){
                if (!AppSetting.getInstance().firstLaunch) save_change_alarm.visibility = View.VISIBLE
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notification_fragment, container,false)
    }

    private fun initBody(){
        alarm.apply {
            changeColorCheckBox(vibrate_cv, vibrate_tv, vibrate_cb, vibrate)
            save_change_alarm.apply {
                setOnClickListener {
                    AlarmSchedule.apply {
                        set(alarm)
                        get().schedule(context!!)
                    }
                    backProfileFragment()
                }
                // determine show or not save_change_alarm bt when isAlarmChanged changed
                isAlarmChanged = isAlarmChanged
            }
            repeat_bt.apply {
                text = alarm.getDisplayRepeat(context!!)
                val listRepeat = listOf(
                    getString(R.string.every_mon),
                    getString(R.string.every_tue),
                    getString(R.string.every_wed),
                    getString(R.string.every_thu),
                    getString(R.string.every_fri),
                    getString(R.string.every_sat),
                    getString(R.string.every_sun),
                )
                setOnClickListener {
                    BaseBottomSheetDialogFragment(getString(R.string.choose_repeat), this@NotificationFragment).apply {
                        onDialogShow = {
                            val setSelectedPosition = mutableSetOf<Int>().apply{
                                if (mon) add(0)
                                if (tue) add(1)
                                if (wed) add(2)
                                if (thu) add(3)
                                if (fri) add(4)
                                if (sat) add(5)
                                if (sun) add(6)
                            }
                            setContentView(
                                SelectedMultiItemBottomSheetFragment(listRepeat, listOf(), setSelectedPosition, this).also {
                                    it.confirm = {
                                        isAlarmChanged = true
                                        val listSelected = BooleanArray(7)
                                        it.forEach { position ->
                                            listSelected[position] = true
                                        }
                                        listSelected.forEachIndexed { index, isSelected ->
                                            when(index){
                                                0 -> mon = isSelected
                                                1 -> tue = isSelected
                                                2 -> wed = isSelected
                                                3 -> thu = isSelected
                                                4 -> fri = isSelected
                                                5 -> sat = isSelected
                                                6 -> sun = isSelected
                                            }
                                        }
                                        text = getDisplayRepeat(context!!)
                                        isAlarmChanged = true
                                    }
                                }
                            )

                        }
                    }.show()
                }
            }
            vibrate_cv.setOnClickListener {
                isAlarmChanged = true
                vibrate = !vibrate
                changeColorCheckBox(vibrate_cv, vibrate_tv, vibrate_cb, vibrate)
                if (vibrate) {
                    (context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(500)
                }
            }
            first_notification_bt.apply {
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, alarm.hourFirst)
                    set(Calendar.MINUTE, alarm.minuteFirst)
                }
                text = formatTime(cal)
                setOnClickListener {
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, alarm.hourFirst)
                        set(Calendar.MINUTE, alarm.minuteFirst)
                    }
                    BaseBottomSheetDialogFragment(getString(R.string.choose_time), this@NotificationFragment).run {
                        onDialogShow = {
                            setContentView(
                                TimePickerBottomSheetFragment(cal.timeInMillis, this)
                                    .action(TimePickerBottomSheetFragment.ACTION_PICK_TIME)
                                    .also {
                                        it.confirm = { cal ->
                                            isAlarmChanged = true
                                            text = formatTime(cal)
                                            alarm.apply {
                                                hourFirst = cal[Calendar.HOUR_OF_DAY]
                                                minuteFirst = cal[Calendar.MINUTE]
                                            }
                                        }
                                        it.isCheckInvalid = false
                                    }
                            )
                        }
                        show()
                    }
                }
            }
            last_notification_bt.apply {
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, alarm.hourEnd)
                    set(Calendar.MINUTE, alarm.minuteEnd)
                }
                text = formatTime(cal)
                setOnClickListener {
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, alarm.hourEnd)
                        set(Calendar.MINUTE, alarm.minuteEnd)
                    }
                    BaseBottomSheetDialogFragment(getString(R.string.choose_time), this@NotificationFragment).run {
                        onDialogShow = {
                            setContentView(
                                TimePickerBottomSheetFragment(cal.timeInMillis, this)
                                    .action(TimePickerBottomSheetFragment.ACTION_PICK_TIME).also {
                                        it.confirm = {
                                            alarm.hourEnd = it[Calendar.HOUR_OF_DAY]
                                            alarm.minuteEnd = it[Calendar.MINUTE]
                                            isAlarmChanged = true
                                            text = formatTime(it)
                                        }
                                        it.isCheckInvalid = false

                                    }
                            )
                        }
                        show()
                    }
                }
            }
            sound_alarm_bt.apply {
                val displayValue = listOf(getString(R.string._default), getString(R.string.water), getString(R.string.none))
                fun getIndexDisplayValue() = when(alarm.sound) {
                    AlarmSchedule.SOUND_DEFAULT -> 0
                    AlarmSchedule.SOUND_WATER -> 1
                    else -> 2
                }
                text = displayValue[getIndexDisplayValue()]
                setOnClickListener {
                    var sounds = listOf(AlarmSchedule.SOUND_DEFAULT, AlarmSchedule.SOUND_WATER, AlarmSchedule.SOUND_NONE)
                    BaseBottomSheetDialogFragment(getString(R.string.choose_sound), this@NotificationFragment).run {
                        onDialogShow = {
                            setContentView(
                                SelectedBottomSheetFragment(displayValue, listOf(), getIndexDisplayValue(), this).also {
                                    it.onItemChanged {position -> playSound(sounds[position]) }
                                    it.confirm = { index ->
                                        isAlarmChanged = true
                                        sound = sounds[index]
                                        text = displayValue[index]
                                    }
                                }
                            )
                        }
                        show()
                    }
                }
            }
            play_alarm_sound_bt.setOnClickListener { playSound(alarm.sound) }
            interval_bt.apply {
                val arrayTime = arrayOf(getString(R.string._15_minutes), getString(R.string._30_minutes),getString(R.string._45_minutes),  getString(R.string._1_hour), getString(R.string._1h30),
                    getString(R.string._2_hour),  getString(R.string._2h30), getString(R.string._3h),
                    getString(R.string._3h30),  getString(R.string._4h))
                text = when(repeatTime){
                    15 -> arrayTime[0]
                    30 -> arrayTime[1]
                    45 -> arrayTime[2]
                    60 -> arrayTime[3]
                    90 -> arrayTime[4]
                    120 -> arrayTime[5]
                    150 -> arrayTime[6]
                    180 -> arrayTime[7]
                    210 -> arrayTime[8]
                    else ->arrayTime[9]
                }
                setOnClickListener {
                    BaseBottomSheetDialogFragment(getString(R.string.choose_interval), this@NotificationFragment).run {
                        onDialogShow = {
                            setContentView(
                                IntervalPickerBottomSheetFragment(alarm.repeatTime, arrayTime, this).also {
                                    it.confirm = { interval ->
                                        alarm.apply {
                                            repeatTime = interval
                                        }
                                        isAlarmChanged = true
                                        text = when (interval) {
                                            15 -> arrayTime[0]
                                            30 -> arrayTime[1]
                                            45 -> arrayTime[2]
                                            60 -> arrayTime[3]
                                            90 -> arrayTime[4]
                                            120 -> arrayTime[5]
                                            150 -> arrayTime[6]
                                            180 -> arrayTime[7]
                                            210 -> arrayTime[8]
                                            else -> arrayTime[9]
                                        }
                                    }
                                }
                            )
                        }
                        show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()) }

        alarm.apply {
            active_alarm_cb.setOnCheckedChangeListener { _, isChecked ->
                active = isChecked
                if (isChecked){
                    notification_fragment_body.expand(true)
                    initBody()
                }
                else{
                    notification_fragment_body.collapse(true)
                    save_change_alarm.visibility = View.GONE
                }
            }
            changeColorCheckBox(active_alarm_cv, active_alarm_tv, active_alarm_cb, active)
            active_alarm_tv.text = if (active) getString(R.string.turn_on) else getString(R.string.turn_off)
            active_alarm_cv.setOnClickListener {
                active = !active
                active_alarm_tv.text = if (active) getString(R.string.turn_on) else getString(R.string.turn_off)
                changeColorCheckBox(active_alarm_cv, active_alarm_tv, active_alarm_cb, active)
            }
            if (!active){
                notification_fragment_body.collapse(true)
            }
            initBody()
        }
        if (!areNotificationEnable() && !AppSetting.getInstance().firstLaunch){
            BaseBottomSheetDialogFragment(getString(R.string.notification), this).also {
                it.onDialogShow = {
                    it.confirmMessenger(getString(R.string.enable))
                    it.setContentView(NotificationDisableAlert(it))
                }
                   it.show()
            }
        }
    }
    private fun areNotificationEnable(): Boolean{
        return NotificationManagerCompat.from(context!!).areNotificationsEnabled()
    }
    private fun playSound(sound : String){
        val uri = when(sound){
            AlarmSchedule.SOUND_DEFAULT -> Settings.System.DEFAULT_NOTIFICATION_URI
            AlarmSchedule.SOUND_WATER -> Uri.parse("android.resource://" +
                    context!!.packageName + "/"+"${R.raw.water_effect}")
            else -> null
        }
        if (uri != null) {

            try {
                player.apply {
                    reset()
                    setDataSource(context!!, uri)
                    prepare()
                    start()
                }
            }
            catch (e : Exception){

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun backProfileFragment() {
        AlarmSchedule.get().active = alarm.active
        super.backProfileFragment()
    }

    class NotificationDisableAlert(parent : BaseBottomSheetDialogFragment) : BaseBodyDialog<Nothing>(R.layout.notification_disable_alert, parent){
        override fun confirm() {
            val intent = Intent().apply {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context!!.packageName)
                    }
                    else -> {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        addCategory(Intent.CATEGORY_DEFAULT)
                        data = Uri.parse("package:" + context!!.packageName)
                    }
                }
            }
            parent!!.requireActivity().startActivity(intent)
        }
    }
}