package com.phongpn.water.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.phongpn.water.R
import com.phongpn.water.adapter.ListDrinkIconAdapter
import com.phongpn.water.adapter.ListDrinkIconAdapter.DrinkIconData
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.notification.Alarm
import com.phongpn.water.notification.AlarmSchedule
import com.phongpn.water.ui.MainActivity
import com.phongpn.water.ui.base.BaseFragment
import com.phongpn.water.ui.dialog.EditLogBottomSheetFragment
import com.phongpn.water.ui.profile.ProfileFragment
import com.phongpn.water.ui.statistic.StatisticFragment
import com.phongpn.water.util.Drink
import com.phongpn.water.util.formatTime
import com.phongpn.water.viewmodel.LogDrinkViewModel
import com.phongpn.water.viewmodel.MainFragmentUiState
import com.phongpn.water.viewmodel.MainFragmentViewModel
import com.phongpn.water.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : BaseFragment() {
    private val logDrinkViewModel: LogDrinkViewModel by activityViewModels()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()

    override fun onBackPressed() = Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        addObserver()

    }

    private fun setupViews() {
        shortcut_icon_rv.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                    clipChildren = false
                }
            clipChildren = false
            adapter = ShortcutDrinkIconAdapter(emptyList()) {
                mainFragmentViewModel.shortcutIcon.value!![it].apply {
                    vibrate()
                    logDrinkViewModel.insert(LogDrink(amount, type, Calendar.getInstance()))
                }
            }
        }
        addListenerForView()
    }

    private fun addObserver() {
        logDrinkViewModel.totalAll.observe(viewLifecycleOwner) {
            val currentDate = LogDrink.createIdDate(Calendar.getInstance())
            var drinkToday = false
            for (log in it) {
                if (log.idDate == currentDate) {
                    mainFragmentViewModel.changeTotalDrinkToday(log.total!!)
                    drinkToday = true
                    break
                }
            }
            if (!drinkToday) mainFragmentViewModel.changeTotalDrinkToday(0)

        }
        mainFragmentViewModel.apply {
            mainFragmentUiState.observe(viewLifecycleOwner) {
                it?.let { changeWave(it) }
            }

            shortcutIcon.observe(viewLifecycleOwner) {
                it?.let {
                    (shortcut_icon_rv.adapter as ListDrinkIconAdapter).update(it.map {
                        DrinkIconData(it.type, it.amount, it.isSelected)
                    })
                }
                mainFragmentViewModel.saveShortcutIcon(context!!)
            }
        }

        mainViewModel.goal.observe(viewLifecycleOwner) { newGoal ->
            mainFragmentViewModel.changeGoal(newGoal)
        }

        mainViewModel.nextAlarmFire.observe(viewLifecycleOwner) {
            alarm_tv.text =
                formatTime(Calendar.getInstance().apply { timeInMillis = it.time })
        }

//        AlarmSchedule.get().apply {
//            observers = mutableListOf()
//            observe { _, alarm ->
//                alarm as Alarm
//
//            }
//            lifecycleScope.launch {
//                while (true) {
//                    getNextAlarm()?.let { notifyDataChanged(AlarmSchedule.ALARM, it) }
//                    delay(1000)
//                }
//            }
//        }
    }

    private fun changeWave(state: MainFragmentUiState) {
        state.apply {
            wave.setProgress(100 - process.toInt())
            intake_drink_tv.text = intakePreview
            if (process < 100) {
                enough_drink_tv.text = getString(R.string.you_have_to_drink)
                remain_drink_tv.text = "$remainToday ml"
                ratio_drink_tv.apply {
                    text = "${process.toInt()} %"
                    visibility = View.VISIBLE
                    remain_drink_tv.textSize = 14f
                }
            } else {
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
                        mainFragmentViewModel.addShortcutIcon(
                            DrinkIconData(
                                log.type,
                                log.amount,
                                false
                            )
                        )
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
    }

    private fun vibrate() {
        val vibrator = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(longArrayOf(0, 70), -1)
    }

}