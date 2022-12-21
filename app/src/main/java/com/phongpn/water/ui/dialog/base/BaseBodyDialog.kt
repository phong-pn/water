package com.phongpn.water.ui.dialog.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseBodyDialog<T : Any>(@LayoutRes var layoutId : Int? = null, val parent: BaseBottomSheetDialogFragment? = null, var modifier: Modifier? = null) : Fragment(),
    IBodyDialog {
    var onViewCreated : ((BaseBodyDialog<T>)-> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutId?.let { inflater.inflate(it, container, false) }  ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modifier?.let {
            it.mPadding?.let { listPadding ->
                view.setPadding(listPadding[0].toPx(), listPadding[1].toPx(), listPadding[2].toPx(), listPadding[3].toPx())
            }
        }
        onViewCreated?.invoke(this)
    }
    var isCheckInvalid = true
    var confirm: ((T) -> Unit)? = null
    var cancel : (() -> Unit)? = null
    var change: ((T) -> Unit)? = null
    override fun cancel() {
        cancel?.invoke()
    }

    override fun confirm() {
        TODO("Not yet implemented")
    }

    fun onChooseInvalidValue(){
        if (isCheckInvalid)
        parent?.waring()
    }

    fun onChooseValidValue(){
        if (isCheckInvalid)parent?.disableWarning()
    }
    private fun Int.toPx(): Int {
        return this * resources.displayMetrics.density.toInt()
    }

}