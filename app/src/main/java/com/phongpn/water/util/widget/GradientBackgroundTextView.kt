package com.phongpn.water.util.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.phongpn.water.R
import com.phongpn.water.util.changeGradient

class GradientBackgroundTextView(context: Context, attrs : AttributeSet, defStyleAttr : Int)
    : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr){

    constructor(context: Context, attr : AttributeSet) : this(context, attr, 0)

    var onIsChooseChanged : ((Boolean) -> Unit)? = null

    var onClickListener : ((View) -> Unit)? = null


    var isChoose = false
        set(value) {
            onIsChooseChanged?.invoke(value)
            field = value
            if (value) {
                background.setTint(backgroundColorWhenChoose)
                setTextColor(textColorWhenChoose)
            }
            else {
                background.setTintList(null)
                setTextColor(backgroundColorWhenChoose)
            }
            invalidate()
        }

    var startColor : Int = Color.BLUE
    var endColor : Int = Color.BLUE
    var orientation = 0
    var textColorWhenChoose = Color.WHITE
    var backgroundColorWhenChoose = Color.BLUE

    init {
        setTextColor(textColorWhenChoose)
        background = AppCompatResources.getDrawable(context, R.drawable.icon_drink_container)
        context.theme.obtainStyledAttributes(attrs, R.styleable.GradientBackgroundTextView, 0 ,0).apply {
            try {
                startColor = getInt(R.styleable.GradientBackgroundTextView_startColor, startColor)
                endColor = getInt(R.styleable.GradientBackgroundTextView_endColor, endColor)
                orientation = getInt(R.styleable.GradientBackgroundTextView_orientation, 0)
                textColorWhenChoose = getColor(R.styleable.GradientBackgroundTextView_text_color_when_choose, Color.WHITE)
                backgroundColorWhenChoose = getColor(R.styleable.GradientBackgroundTextView_background_color_when_choose, Color.BLUE)
                background.changeGradient(startColor, endColor, when(orientation){
                    90 -> GradientDrawable.Orientation.BOTTOM_TOP
                    180 -> GradientDrawable.Orientation.RIGHT_LEFT
                    270 -> GradientDrawable.Orientation.TOP_BOTTOM
                    else -> GradientDrawable.Orientation.LEFT_RIGHT
                })
                isChoose = getBoolean(R.styleable.GradientBackgroundTextView_isChoose, false)
            }
            finally {
                recycle()
            }
        }

        setOnClickListener {
            if (!isChoose) isChoose = true
            onClickListener?.invoke(this)
        }

    }
}