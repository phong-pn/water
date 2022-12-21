package com.phongpn.water.ui.dialog.base

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.phongpn.water.R

abstract class ContainerDialog(var fm: FragmentManager): DialogFragment(), Dialog {
    lateinit var body: BaseBodyDialog<Any>
    lateinit var mPositiveBt : Button
    lateinit var mNegativeBt : TextView
    lateinit var mTitleTv : TextView
    var mBodyContainer: Int = 0

    override fun setPositiveButton(text: String, show: Boolean, onClick: ((View) -> Unit)?){
        mPositiveBt.apply {
            this.text = text
            setOnClickListener {
               onClick?.invoke(it)
            }
        }
        mPositiveBt.visibility = if (show) View.VISIBLE else View.GONE
    }


    override fun setNegativeButtonOnClick(onClick: (View) -> Unit) {
        mNegativeBt.setOnClickListener { onClick(it) }
    }

    override fun setPositionButtonOnClick(onClick: (View) -> Unit) {
        mPositiveBt.setOnClickListener { onClick(it) }
    }


    override fun setNegativeButton(text: String, show: Boolean, onClick: ((View) -> Unit)?){
        mNegativeBt.apply {
            this.text = text
            onClick?.let {
                setOnClickListener(it)
            }
        }
        mNegativeBt.visibility = if (show) View.VISIBLE else View.GONE

    }

    override fun title(text: String?, color: Int?, show: Boolean){
        if (show) {
            mTitleTv.visibility = View.VISIBLE
            text?.let { mTitleTv.text = it }
            color?.let { mTitleTv.setTextColor(it) }
        }
        else mTitleTv.visibility = View.GONE
    }

    override fun setContentView(view: BaseBodyDialog<Any>){
        body = view
        childFragmentManager.beginTransaction()
            .replace(R.id.content_view, body, null)
            .commitNow()
    }

    override fun setContentView(@LayoutRes resId: Int){
        setContentView(BaseBodyDialog(resId))
    }

    override fun setContentView(@LayoutRes resId: Int, modifier: Modifier){
        setContentView(BaseBodyDialog(resId, modifier = modifier))
    }

    override fun show() {
        show(fm, null)
    }
}