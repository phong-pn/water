package com.phongpn.water.ui.home

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.phongpn.water.R
import com.phongpn.water.adapter.ListDrinkIconAdapter
import com.phongpn.water.adapter.ListDrinkIconAdapter.*
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.notification.Alarm
import com.phongpn.water.notification.AlarmSchedule
import com.phongpn.water.ui.MainActivity
import com.phongpn.water.ui.base.BaseFragment
import com.phongpn.water.ui.dialog.EditLogBottomSheetFragment
import com.phongpn.water.ui.profile.ProfileFragment
import com.phongpn.water.ui.statistic.StatisticFragment
import com.phongpn.water.util.*
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.constant.params.OZ_UK
import com.phongpn.water.util.constant.params.OZ_US
import com.phongpn.water.util.profileparams.AppSetting
import com.phongpn.water.util.profileparams.UnitParams
import com.phongpn.water.util.profileparams.WaterIntakeParams
import com.phongpn.water.viewmodel.LogDrinkViewModel
import com.phongpn.water.viewmodel.MainFragmentViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : BaseFragment() {
    private val logDrinkViewModel : LogDrinkViewModel by activityViewModels()

    private val mainFragmentViewModel : MainFragmentViewModel by viewModels ()

    private val waterIntakeParams = WaterIntakeParams.getInstance()
    private val unitParams = UnitParams.getInstance()
    override fun onBackPressed() = Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mainFragmentViewModel.apply {
            isActivateDay.value = waterIntakeParams.activeDay
            isHotDay.value = waterIntakeParams.hotDay
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unitParams.observers = mutableListOf()
        waterIntakeParams.observers = mutableListOf()
        AlarmSchedule.get().apply {
            observers = mutableListOf()
            observe { _, alarm ->
                alarm as Alarm
                alarm_tv.text = formatTime(Calendar.getInstance().apply { timeInMillis = alarm.time } )
            }
            lifecycleScope.launch {
                while (true) {
                    getNextAlarm()?.let { notifyDataChanged(AlarmSchedule.ALARM, it) }
                    delay(1000)
                }
            }
        }
        setupViews()
        addObserver()

    }

    private fun setupViews() {
        shortcut_icon_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                clipChildren = false
            }
            clipChildren = false
            adapter = ShortcutDrinkIconAdapter(emptyList()){
                mainFragmentViewModel.shortcutIcon.value!![it].apply {
                    vibrate()
                    logDrinkViewModel.insert( LogDrink(amount, type, Calendar.getInstance()))
                }
            }
        }
        addListenerForView()
    }

    private fun addObserver(){
        logDrinkViewModel.totalAll.observe(viewLifecycleOwner){
            val currentDate = LogDrink.createIdDate(Calendar.getInstance())
            var drinkToday = false
            for (log in it){
                if (log.idDate == currentDate) {
                    mainFragmentViewModel.currentTotalDrinkToday.postValue(log.total)
                    drinkToday = true
                    break
                }
            }
            if (!drinkToday) mainFragmentViewModel.currentTotalDrinkToday.postValue(0)

        }
        mainFragmentViewModel.apply {
            currentTotalDrinkToday.observe(viewLifecycleOwner){ total->
                changeWave(total, waterIntakeParams.amount)
            }
            unitDrink.observe(viewLifecycleOwner){
                currentTotalDrinkToday.value?.let { total -> changeWave(total, waterIntakeParams.amount) }
            }
            totalDrinkToday.observe(viewLifecycleOwner){totalToday ->
                currentTotalDrinkToday.value?.let { currentTotal ->
                    changeWave(currentTotal, totalToday)
                }
            }
            unitParams.observe { type, data -> when(type){
                UnitParams.UNIT_DRINK -> unitDrink.postValue(data as String)
                }
            }

            waterIntakeParams.observe { type, data -> when(type){
                WaterIntakeParams.AMOUNT -> totalDrinkToday.postValue(data as Int)
                }
            }


            val blue = ContextCompat.getColor(context!!, R.color.blue)
            isHotDay.observe(viewLifecycleOwner){
                if (it) {
                    waterIntakeParams.hotDay = true
                    hot_day_tv.setTextColor(Color.WHITE)
                    ((hot_day_frame.background as RippleDrawable).getDrawable(0)).changeGradient(
                        Color.parseColor("#014DFF"),
                        Color.parseColor("#23A9FF"),
                        GradientDrawable.Orientation.RIGHT_LEFT
                    )
                    icon_hot_day.apply {
                        setColorFilter(blue)
                        background.setTint(Color.WHITE)
                    }
                }
                else {
                    waterIntakeParams.hotDay  = false
                    hot_day_tv.setTextColor(blue)
                    hot_day_frame.background.setTint(Color.WHITE)
                    icon_hot_day.apply {
                        setColorFilter(Color.WHITE)
                        background.changeGradient(
                            Color.parseColor("#014DFF"),
                            Color.parseColor("#23A9FF"),
                            GradientDrawable.Orientation.BOTTOM_TOP
                        )
                    }
                }
            }

            isActivateDay.observe(viewLifecycleOwner){
                if (it) {
                    waterIntakeParams.activeDay = true
                    active_tv.setTextColor(Color.WHITE)
                    (activity_frame.background as RippleDrawable).getDrawable(0).changeGradient(
                        Color.parseColor("#014DFF"),
                        Color.parseColor("#23A9FF"),
                        GradientDrawable.Orientation.RIGHT_LEFT
                    )
                    icon_activity.apply {
                        setColorFilter(blue)
                        background.setTint(Color.WHITE)
                    }
                }
                else {
                    waterIntakeParams.activeDay = false
                    active_tv.setTextColor(blue)
                    activity_frame.background.setTint(Color.WHITE)
                    icon_activity.apply {
                        setColorFilter(Color.WHITE)
                        background.changeGradient(
                            Color.parseColor("#014DFF"),
                            Color.parseColor("#23A9FF"),
                            GradientDrawable.Orientation.BOTTOM_TOP
                        )
                    }
                }
            }

            shortcutIcon.observe(viewLifecycleOwner){
                it?.let {
                    (shortcut_icon_rv.adapter as ListDrinkIconAdapter).update(it.map {
                        DrinkIconData(it.type, it.amount, it.isSelected)
                    })
                }
                mainFragmentViewModel.saveShortcutIcon(context!!)
            }
        }
    }
    private fun changeWave(current: Int, des: Int){
        val ratio = ((current/ des.toFloat()) * 100).toInt().apply {
            wave.setProgress(100 - this)
        }
        val mCurrent = when(unitParams.unitDrink){
            OZ_US -> current.toOz_Us(ML)
            OZ_UK -> current.toOz_Uk(ML)
            else -> current
        }
        val mTotal = when(unitParams.unitDrink){
            OZ_US -> des.toOz_Us(ML)
            OZ_UK -> des.toOz_Uk(ML)
            else -> des
        }
        intake_drink_tv.text = "$mCurrent ${unitParams.unitDrink} / $mTotal ${unitParams.unitDrink} "
        (mTotal - mCurrent).apply {
            if (this > 0) {
                enough_drink_tv.text = getString(R.string.you_have_to_drink)
                remain_drink_tv.text = "$this ${unitParams.unitDrink}"
                remain_drink_tv.invalidate()
                ratio_drink_tv.apply {
                    setText("$ratio %")
                    visibility = View.VISIBLE
                    remain_drink_tv.textSize = 14f
                }

            }
            else{
                enough_drink_tv.text = getString(R.string.enough_drink)
                remain_drink_tv.apply {
                    textSize = 28f
                    text = getString(R.string.keep_it_that_way)
                    invalidate()
                }
                ratio_drink_tv.visibility = View.GONE
            }
        }
    }
    private fun addListenerForView() {
        rippleBackground.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                EditLogBottomSheetFragment(
                    EditLogBottomSheetFragment.ACTION_ADD,
                    LogDrink(
                        200,
                        Drink.WATER,
                        Calendar.getInstance()
                    )
                )
                    .onInsertLog { log ->
                        logDrinkViewModel.insert(log)
                        mainFragmentViewModel.addShortcutIcon(DrinkIconData(log.type, log.amount, false))
                    }
                    .show(childFragmentManager, null)
            }
        }


        statistic_bt.setOnClickListener {
            (requireActivity() as MainActivity).toBaseFragment<StatisticFragment> {
                enter = R.anim.right_to_left
            }
        }

        profile_bt.setOnClickListener {
            (requireActivity() as MainActivity).toBaseFragment<ProfileFragment> {
                enter = R.anim.left_to_right
            }
        }

        hot_day_frame.setOnClickListener {
            mainFragmentViewModel.isHotDay.apply {
                value = !value!!
                waterIntakeParams.hotDay = value!!
                postValue(value!!)
                if (value == true)
                    Toast.makeText(
                        context,
                        getString(R.string.daily_norm) + "+${formatDrinkUnit(500, ML)} " + "( ${getString(R.string.hot_day)})",
                        Toast.LENGTH_SHORT).show()
            }
        }

        activity_frame.setOnClickListener {
            mainFragmentViewModel.isActivateDay.apply {
                value = !value!!
                waterIntakeParams.activeDay = value!!
                postValue(value!!)
                if (value == true)
                    Toast.makeText(
                        context,
                        getString(R.string.daily_norm) + "+${formatDrinkUnit(500, ML)} " + "( ${getString(R.string.active)})",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun vibrate(){
        val vibrator = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(longArrayOf(0,70), -1)
    }

}