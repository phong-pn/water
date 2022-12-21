package com.phongpn.water.ui.dialog.base

import android.view.View
import androidx.annotation.LayoutRes

interface Dialog {
    fun setPositiveButton(text: String, show: Boolean = true, onClick: ((View) -> Unit)? = null)

    fun setPositionButtonOnClick(onClick: (View) -> Unit)

    fun setNegativeButtonOnClick(onClick: (View) -> Unit)

    fun setNegativeButton(text: String, show: Boolean = true, onClick: ((View) -> Unit)? = null)

    fun title(text: String? = null, color: Int? = null, show: Boolean = true)

    fun setContentView(view: BaseBodyDialog<Any>)

    fun setContentView(@LayoutRes resId: Int)

    fun setContentView(@LayoutRes resId: Int, modifier: Modifier)

    fun show()

}