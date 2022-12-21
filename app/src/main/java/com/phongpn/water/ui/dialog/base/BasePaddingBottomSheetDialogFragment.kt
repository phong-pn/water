package com.phongpn.water.ui.dialog.base

import android.app.Dialog
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.setPadding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.phongpn.water.R

open class BasePaddingBottomSheetDialogFragment : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.apply {
            setOnShowListener {
                findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)!!.apply{
                    setBackgroundResource(R.drawable.container_corner_32dp)
                    setPadding(48)
                }
            }
            behavior.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                this.isDraggable = false
            }
        }
        return dialog
    }
}