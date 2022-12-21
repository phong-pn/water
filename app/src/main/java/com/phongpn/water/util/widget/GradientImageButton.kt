package com.phongpn.water.util.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import com.phongpn.water.R
import com.phongpn.water.util.changeGradient

class GradientImageButton(context: Context, attr : AttributeSet, defStyleAttr : Int) : AppCompatImageButton(context, attr, defStyleAttr) {
    constructor(context: Context, attr : AttributeSet) : this(context, attr, 0)

    var onIsChooseChanged : ((Boolean) -> Unit)? = null

    var onClickListener : ((View) -> Unit)? = null

    private var isChoose = false
        set(value) {
            onIsChooseChanged?.invoke(value)
            field = value
            if (value) {
                background.setTint(backgroundColorWhenChoose)
                setColorFilter(iconColorWhenChoose)
            }
            else {
                background.setTintList(null)
                setColorFilter(backgroundColorWhenChoose)
            }
        }

    var startColor : Int = Color.BLUE
    var endColor : Int = Color.BLUE
    var orientation = 0
    var iconColorWhenChoose = Color.WHITE
    var backgroundColorWhenChoose = Color.BLUE

    init {
        background = AppCompatResources.getDrawable(context, R.drawable.icon_drink_container)
        context.theme.obtainStyledAttributes(attr, R.styleable.GradientImageButton, 0 ,0).apply {
            try {
                startColor = getInt(R.styleable.GradientImageButton_startColor, startColor)
                endColor = getInt(R.styleable.GradientImageButton_endColor, endColor)
                orientation = getInt(R.styleable.GradientImageButton_orientation, 0)
                iconColorWhenChoose = getColor(R.styleable.GradientImageButton_icon_color_when_choose, Color.WHITE)
                backgroundColorWhenChoose = getColor(R.styleable.GradientImageButton_background_color_when_choose, Color.BLUE)
                background.changeGradient(startColor, endColor, when(orientation){
                    90 -> GradientDrawable.Orientation.BOTTOM_TOP
                    180 -> GradientDrawable.Orientation.RIGHT_LEFT
                    270 -> GradientDrawable.Orientation.TOP_BOTTOM
                    else -> GradientDrawable.Orientation.LEFT_RIGHT
                })
                isChoose = getBoolean(R.styleable.GradientImageButton_isChoose, false)
            }
            finally {
                recycle()
            }
        }

//        setOnClickListener {
//            if (!isChoose) isChoose = true
//            onClickListener?.invoke(this)
//        }


        }

}