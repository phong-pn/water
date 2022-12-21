package com.phongpn.water.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.phongpn.water.R
import com.phongpn.water.ui.dialog.base.AlertDialogFragment
import com.phongpn.water.ui.dialog.base.Modifier

class ExitAppDialogFragment(
    modifier: Modifier = Modifier(),
    fm: FragmentManager,

    onShow : AlertDialogFragment.() -> Unit): AlertDialogFragment(modifier, R.layout.exit_pop_up_diolog, fm, onShow) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}