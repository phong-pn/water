package com.phongpn.water.ui.dialog.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.phongpn.water.R

open class AlertDialogFragment(
    var modifier: Modifier = Modifier(),
    var resId: Int,
    fm: FragmentManager,
    var onShow : AlertDialogFragment.() -> Unit
) : ContainerDialog(fm) {
    constructor(modifier: Modifier, resId: Int, fragment: Fragment, onShow: AlertDialogFragment.() -> Unit): this(modifier, resId, fragment.childFragmentManager, onShow)
    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return rootView
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cornerBackground = modifier.mCorner?.let {
            MaterialShapeDrawable(
                ShapeAppearanceModel().toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, it[0].toPx().toFloat())
                    .setTopRightCorner(CornerFamily.ROUNDED, it[1].toPx().toFloat())
                    .setBottomLeftCorner(CornerFamily.ROUNDED, it[2].toPx().toFloat())
                    .setBottomRightCorner(CornerFamily.ROUNDED, it[3].toPx().toFloat())
                    .build()
            ).apply {
                fillColor = ContextCompat.getColorStateList(requireContext(), R.color.white)
            }
        }
        rootView = LayoutInflater.from(requireContext()).inflate(resId, null)
        val dialog =  MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MaterialComponents_BottomSheetDialog)
            .setView(rootView)
            .apply { cornerBackground?.let { background = it } }
            .create()
            .apply {
                setOnShowListener {
                    dialog?.apply {
                        findViewById<FrameLayout>(R.id.custom)!!.apply {
                            modifier.apply {
                                mPadding?.let {
                                    setPadding(it[0].toPx(), it[1].toPx(), it[2].toPx(), it[3].toPx())
                                }
                                mMargin?.let {
                                    ViewGroup.MarginLayoutParams(layoutParams)
                                        .setMargins(it[0].toPx(), it[1].toPx(), it[2].toPx(), it[3].toPx())
                                }
                            }
                        }
                    }

                }

            }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onShow()
    }

    override fun onResume() {
        super.onResume()
        requireDialog().window?.apply {
            attributes = attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }
    }

    fun Int.toPx(): Int {
        return this * resources.displayMetrics.density.toInt()
    }
}