package com.phongpn.water.ui.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.phongpn.water.R
import com.phongpn.water.ui.dialog.base.BaseBottomSheetDialogFragment
import com.phongpn.water.ui.dialog.base.BaseBodyDialog
import kotlinx.android.synthetic.main.select_interval_dialog_bottom_sheet.*

class IntervalPickerBottomSheetFragment(
    private var repeatTime : Int,
    private val arrayTime : Array<String>,
    parent : BaseBottomSheetDialogFragment
) : BaseBodyDialog<Int>(R.layout.select_interval_dialog_bottom_sheet, parent){
    override fun confirm() {
        confirm?.invoke(repeatTime)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        interval_picker.apply {
            typeface = Typeface.DEFAULT_BOLD
            setSelectedTypeface(Typeface.DEFAULT_BOLD)
            minValue = 0
            maxValue = arrayTime.size-1
            displayedValues = arrayTime
            value = when(repeatTime){
                15 -> 0
                30 -> 1
                45 -> 2
                60 -> 3
                90 -> 4
                120 -> 5
                150 -> 6
                180 -> 7
                210 -> 8
                else ->9
            }
            setOnValueChangedListener { _, _, newVal ->
                repeatTime = when(newVal){
                    0 -> 15
                    1 -> 30
                    2 -> 45
                    3 -> 60
                    4 -> 90
                    5 -> 120
                    6 -> 150
                    7 -> 180
                    8 -> 210
                    else -> 240
                }
            }
        }
    }
    }
