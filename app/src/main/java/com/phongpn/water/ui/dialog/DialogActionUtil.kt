package com.phongpn.water.ui.dialog

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.phongpn.water.R
import com.phongpn.water.ui.dialog.base.AlertDialogFragment
import com.phongpn.water.ui.dialog.base.Modifier
import kotlinx.android.synthetic.main.base_dialog_bottom_sheet_framgent.*
import kotlinx.android.synthetic.main.exit_pop_up_diolog.*

fun showAlertDialog(
    modifier: Modifier = Modifier(),
    parent: Fragment,
    onShow: AlertDialogFragment.() -> Unit
) = DefaultAlertDialogFragment(modifier, parent) {
    mPositiveBt = confirm_bt.apply { visibility = View.GONE }
    mNegativeBt = cancel_bt.apply { visibility = View.GONE }
    mTitleTv = title_dialog_tv.apply { visibility = View.GONE }
    onShow()
}.show()

fun showExitDialogFragment(
    modifier: Modifier = Modifier(),
//    ad: ProxNativeAd,
    fm: FragmentManager,
    onShow: AlertDialogFragment.() -> Unit
) = ExitAppDialogFragment(modifier, fm) {
    mPositiveBt = exit_app_bt
    mNegativeBt = cancel_exit_app_bt
    onShow()
}.show()

class DefaultAlertDialogFragment(
    modifier: Modifier = Modifier(),
    parent: Fragment,
    onShow: AlertDialogFragment.() -> Unit
) : AlertDialogFragment(modifier, R.layout.base_dialog_bottom_sheet_framgent, parent, onShow)
