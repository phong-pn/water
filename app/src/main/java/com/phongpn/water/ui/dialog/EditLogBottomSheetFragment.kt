package com.phongpn.water.ui.dialog

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.phongpn.water.R
import com.phongpn.water.adapter.ListDrinkIconAdapter
import com.phongpn.water.adapter.ListDrinkIconAdapter.DrinkIconData
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.ui.dialog.base.BaseBottomSheetDialogFragment
import com.phongpn.water.ui.dialog.base.Modifier
import com.phongpn.water.util.Drink
import com.phongpn.water.util.formatDate
import com.phongpn.water.util.formatTime
import com.phongpn.water.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.amount_water_picker.*
import kotlinx.android.synthetic.main.edit_log_bottom_sheet.*
import java.util.*


class EditLogBottomSheetFragment(
    private val action: String,
    private val log: LogDrink?
) : BottomSheetDialogFragment() {
    companion object {
        const val ACTION_EDIT = "EDIT"
        const val ACTION_ADD = "ADD"
    }

    private var delete: ((LogDrink) -> Unit)? = null
    private var insert: ((LogDrink) -> Unit)? = null
    private var update: ((LogDrink) -> Unit)? = null
    private var previousTypeSelected = 0
    private lateinit var _log: LogDrink

    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _log = log?.copy(id = log.id, amount = log.amount, type = log.type, cal = log.cal)
            ?: LogDrink(type = Drink.WATER, cal = Calendar.getInstance(), amount = 200)
        return inflater.inflate(R.layout.edit_log_bottom_sheet, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.apply {
            setOnShowListener {
                findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)!!.setBackgroundResource(
                    R.drawable.container_corner_top
                )
            }
            behavior.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                this.isDraggable = false
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupPicker()
        log_time.text = formatTime(_log.cal)
        log_date_tv.text = formatDate(_log.cal)
        when (action) {
            ACTION_EDIT -> {
                add_bt.text = getString(R.string.update)
            }
            ACTION_ADD -> {
                add_bt.text = getString(R.string.add)
                delete_bt.visibility = View.GONE
            }
        }
        drink_icon_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            Drink.apply {
                val list = listOf(
                    DrinkIconData(WATER, getString(R.string.water), false),
                    DrinkIconData(COFFEE, getString(R.string.coffee), false),
                    DrinkIconData(TEA, getString(R.string.tea), false),
                    DrinkIconData(SOFT_DRINK, getString(R.string.soft_drink), false),
                    DrinkIconData(JUICE, getString(R.string.juice), false),
                    DrinkIconData(PLANT_MILK, getString(R.string.plant_milk), false),
                    DrinkIconData(MILK, getString(R.string.milk), false),
                    DrinkIconData(BEER, getString(R.string.beer), false),
                    DrinkIconData(WINE, getString(R.string.wine), false),
                    DrinkIconData(SPIRITS, getString(R.string.spirits), false)
                ).apply {
                    for (index in indices) {
                        if (get(index).type == _log.type) {
                            get(index).isSelected = true
                            previousTypeSelected = index
                            break
                        }
                    }
                }


                adapter = ListDrinkIconAdapter(list) { pos ->
                    if (previousTypeSelected != pos) {
                        val newList = (adapter as ListDrinkIconAdapter).mListData.map {
                            DrinkIconData(it.type, it.detail, it.isSelected)
                        }
                        newList[previousTypeSelected].isSelected = false
                        previousTypeSelected = pos
                        newList[pos].apply {
                            _log.type = type
                            if (!isSelected) isSelected = true
                        }
                        (adapter as ListDrinkIconAdapter).update(newList)
                    }

                }
            }
        }
        addListenerForViews()
        addObservers()

    }

    private fun addObservers() {

    }

    private fun addListenerForViews() {
        add_bt.setOnClickListener {
            when (action) {
                ACTION_ADD -> {
                    insert?.invoke(_log)
                }
                ACTION_EDIT -> update?.invoke(_log)
            }
            dismiss()
        }
        dismiss_edit_log_bottom_sheet_bt.setOnClickListener { dismiss() }

        log_date_frame.setOnClickListener {
            showDatePicker()
        }

        log_time_edit_frame.setOnClickListener {
            showTimePicker()
        }
        delete_bt?.setOnClickListener {
            delete?.invoke(_log)
            dismiss()
        }
        drink_detail_iv.setOnClickListener {
            showAlertDialog(Modifier { corner(20) }, this) {
                setContentView(R.layout.drink_type_dialog, Modifier { padding(16) })
                setPositiveButton(getString(R.string.got_it)) {
                    dismiss()
                }
            }
        }
    }

    private fun setupPicker() {
        amount_picker.apply {
            typeface = Typeface.DEFAULT_BOLD
            setSelectedTypeface(typeface)
            var valueDisplay = arrayOf<String>()
            for (i in 10..5000 step 10) {
                valueDisplay += i.toString()
            }
            displayedValues = valueDisplay
            maxValue = valueDisplay.size - 1
            minValue = 0
            value = valueDisplay.indexOf(_log.amount.toString())
            setOnValueChangedListener { _, _, newVal ->
                _log.amount = valueDisplay[newVal].toInt()
            }
        }
    }

    private fun showDatePicker() {
        BaseBottomSheetDialogFragment(getString(R.string.choose_date), this).also {
            it.onDialogShow = {
                it.setContentView(
                    TimePickerBottomSheetFragment(_log.cal.timeInMillis, it).also { timePicker ->
                        timePicker.action(TimePickerBottomSheetFragment.ACTION_PICK_DATE)
                        timePicker.onTimeChange { calendar ->
                            log_date_tv.text = formatDate(calendar)
                        }
                        timePicker.confirm = { calendar -> _log.cal = calendar }
                        timePicker.cancel = { log_date_tv.text = formatDate(_log.cal) }
                    }
                )
            }
        }.show()
    }

    private fun showTimePicker() {
        BaseBottomSheetDialogFragment(getString(R.string.choose_time), this).also {
            it.onDialogShow = {
                it.setContentView(
                    TimePickerBottomSheetFragment(_log.cal.timeInMillis, it).also { timePicker ->
                        timePicker.action(TimePickerBottomSheetFragment.ACTION_PICK_TIME)
                        timePicker.onTimeChange { calendar -> log_time.text = formatTime(calendar) }
                        timePicker.confirm = { calendar -> _log.cal = calendar }
                        timePicker.cancel = { log_time.text = formatTime(_log.cal) }
                    }
                )
            }
        }.show()
    }

    fun onDeleteLog(listener: (LogDrink) -> Unit) = this.apply { delete = listener }
    fun onUpdateLog(listener: (LogDrink) -> Unit) = this.apply { update = listener }
    fun onInsertLog(listener: (LogDrink) -> Unit) = this.apply { insert = listener }


}