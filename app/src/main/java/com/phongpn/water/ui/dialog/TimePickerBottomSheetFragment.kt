package com.phongpn.water.ui.dialog

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.phongpn.water.R
import com.phongpn.water.ui.dialog.base.BaseBottomSheetDialogFragment
import com.phongpn.water.ui.dialog.base.BaseBodyDialog
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.date_picker_layout.*
import kotlinx.android.synthetic.main.edit_log_bottom_sheet.*
import kotlinx.android.synthetic.main.time_picker_layout.*
import java.util.*

class TimePickerBottomSheetFragment constructor(
    private val millis : Long,
    parent: BaseBottomSheetDialogFragment
): BaseBodyDialog<Calendar>(R.layout.time_picker_layout, parent), NumberPicker.OnValueChangeListener {

    companion object{
        const val ACTION_PICK_TIME = "TIME"
        const val ACTION_PICK_DATE = "DATE"
    }

    private var _action: String? = null
    private val cal = Calendar.getInstance().apply { timeInMillis = millis }
    private var timeChange : ((Calendar) -> Unit)? = null
    private val typefaceBold = Typeface.DEFAULT_BOLD

    fun action(action: String) : TimePickerBottomSheetFragment = this.apply { _action = action }

    fun onTimeChange(listener: (Calendar) ->Unit) = this.apply { timeChange = listener }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(_action){
            ACTION_PICK_TIME -> setUpTimePicker()
            ACTION_PICK_DATE -> setupDatePicker()
        }
    }

    override fun confirm() {
        confirm?.invoke(cal)
    }

    private fun setupDatePicker(){

        time_picker.visibility = View.GONE
        cal.also {
            log_year_picker.apply {
                typeface = typefaceBold
                setSelectedTypeface(typefaceBold)
                minValue = 2020
                maxValue = 2100
                value = it[Calendar.YEAR]
                setOnValueChangedListener(this@TimePickerBottomSheetFragment)
                setFormatter { year -> year.toString()}
            }

            log_month_picker.apply {
                typeface = typefaceBold
                setSelectedTypeface(typeface)
                var displayValue = arrayOf<String>()
                val _cal = Calendar.getInstance()
                for (month in Calendar.JANUARY until Calendar.DECEMBER+1) {
                    _cal.set(Calendar.MONTH, month)
                    displayValue += _cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                }
                minValue = Calendar.JANUARY
                maxValue = displayValue.size-1
                displayedValues = displayValue
                value = cal[Calendar.MONTH]
                setOnValueChangedListener(this@TimePickerBottomSheetFragment)

            }
            setupDayOfDatePicker()
            log_day_picker.apply {
                typeface = typefaceBold
                setSelectedTypeface(typeface)
                setOnValueChangedListener(this@TimePickerBottomSheetFragment)
                setFormatter { day -> String.format("%02d", day)}
                value = cal[Calendar.DAY_OF_MONTH] }

        }
    }

    private fun setupDayOfDatePicker(){
        log_day_picker.apply {
            minValue = 1
            maxValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            this.invalidate()
        }
    }

    private fun setUpTimePicker(){
        date_picker.visibility = View.GONE
        log_hour_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typeface)
            minValue = 0
            maxValue = 23
            value = cal[Calendar.HOUR_OF_DAY]
            setFormatter { hour -> String.format("%02d", hour) }
            setOnValueChangedListener(this@TimePickerBottomSheetFragment)
        }
        log_minute_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typeface)
            minValue = 0
            maxValue = 59
            value = cal[Calendar.MINUTE]
            setFormatter { minute -> String.format("%02d", minute) }
            setOnValueChangedListener(this@TimePickerBottomSheetFragment)
        }
    }



    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        when(picker){
            log_year_picker  -> {
                cal.set(Calendar.YEAR, newVal)
                setupDayOfDatePicker()
            }
            log_month_picker -> {
                cal.set(Calendar.MONTH, newVal)
                setupDayOfDatePicker()

            }
            log_day_picker -> cal.set(Calendar.DAY_OF_MONTH, newVal)
            log_hour_picker ->  cal.set(Calendar.HOUR_OF_DAY, newVal)
            log_minute_picker ->  cal.set(Calendar.MINUTE, newVal)

        }
        timeChange?.invoke(cal)
    }

}