package com.phongpn.water.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.phongpn.water.R
import com.phongpn.water.adapter.DayLogDrinkAdapter
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.ui.MainActivity
import com.phongpn.water.ui.base.BaseFragment
import com.phongpn.water.ui.dialog.EditLogBottomSheetFragment
import com.phongpn.water.viewmodel.LogDrinkViewModel
import kotlinx.android.synthetic.main.fragment_statistic.*

class StatisticFragment() : BaseFragment() {
    private val logDrinkViewModel: LogDrinkViewModel by activityViewModels()
    private lateinit var logDrinkAdapter: DayLogDrinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        logDrinkViewModel.getLogs()
        logDrinkAdapter = DayLogDrinkAdapter(emptyList()) {
            EditLogBottomSheetFragment(EditLogBottomSheetFragment.ACTION_EDIT, it)
                .onDeleteLog { logDrinkViewModel.delete(it) }
                .onUpdateLog { logDrinkViewModel.update(it) }
                .show(childFragmentManager, null)
        }
        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }

    private fun setupViews() {
        all_log_drink_rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = logDrinkAdapter
            setHasFixedSize(true)
        }
        back_home_bt.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        (requireActivity() as MainActivity).toHomeFragment {
            enter = R.anim.left_to_right
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        logDrinkViewModel.totalAll.observe(viewLifecycleOwner) { allLog ->
        }
        logDrinkViewModel.allLog.observe(viewLifecycleOwner) {
            no_data_alert_tv.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
            logDrinkAdapter.update((groupByDay(it)))
        }
    }

    private fun groupByDay(list: List<LogDrink>): List<List<LogDrink>> {
        val result = mutableListOf<List<LogDrink>>()
        var _log: LogDrink? = null
        var listByDay = mutableListOf<LogDrink>()
        for (index in list.indices) {
            val log = list[index]
            if (_log == null) {
                _log = log
                listByDay += _log
            } else {
                if (_log.idDate != log.idDate) {
                    result += listByDay
                    listByDay = mutableListOf()
                    _log = log
                    listByDay += _log
                } else {
                    _log = log
                    listByDay += _log
                }
            }
            if (index == list.size - 1) result += listByDay
        }
        return result
    }
}
