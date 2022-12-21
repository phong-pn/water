package com.phongpn.water.ui.dialog.base

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.phongpn.water.R
import kotlinx.android.synthetic.main.base_dialog_bottom_sheet_framgent.*

open class BaseBottomSheetDialogFragment(var title : String, var parent : Fragment): BasePaddingBottomSheetDialogFragment() {
    private var shouldInvokeCancel = true
    var warningMessenger : String? = null
    lateinit var onDialogShow : () -> Unit

    fun hideCancelButton(){
        cancel_bt.visibility = View.GONE
    }
    fun hideTitle(){
        title_dialog_tv.visibility = View.GONE
    }

    fun title(title: String) {
        this.title = title
    }

    fun confirmMessenger(message: String){
        confirm_bt.text = message
    }
    private lateinit var content : IBodyDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_dialog_bottom_sheet_framgent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_dialog_tv.text = title
        cancel_bt.setOnClickListener {
            shouldInvokeCancel = true
            dismiss()
        }
        confirm_bt.setOnClickListener {
            shouldInvokeCancel = false
            content.confirm()
            dismiss()
        }
        try {
            onDialogShow.invoke()
        }
        catch (e : Exception){
            Log.d("BaseBottomSheetDialogFragment", "not implement onDialogShow ")
            e.printStackTrace()
        }
    }
    fun show() = this.apply { show(parent.childFragmentManager, null) }

    fun waring(){
        confirm_bt.apply {
            alpha = 0.5f
            isEnabled = false
        }
        warning_tv.text = warningMessenger
    }

    fun disableWarning(){
        confirm_bt.apply {
            alpha = 1f
            isEnabled = true
        }
        warning_tv.text = null
    }

    fun setContentView(view: IBodyDialog) {
        content = view
        childFragmentManager.beginTransaction().add(R.id.content_view, content as BaseBodyDialog<*>, null).commit()
    }

    fun setContentView(@LayoutRes resId : Int) {
        content = object : BaseBodyDialog<Any>(resId, this){
            override fun confirm() {
                TODO("Not yet implemented")
            }
        }
        setContentView(content)
    }

    fun setContentView(@LayoutRes resId : Int, onViewCreated: BaseBodyDialog<Any>.() -> Unit){
        content = BaseBodyDialog<Any>(resId, this).apply {
            this.onViewCreated = onViewCreated
        }
        setContentView(content)
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (shouldInvokeCancel) content.cancel()
    }
}