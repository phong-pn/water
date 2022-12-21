package com.phongpn.water.util.widget

import android.content.Context
import android.util.AttributeSet
import com.skyfishjy.library.RippleBackground

class AutoRippleBackground(context: Context, attrs: AttributeSet?, defStyle: Int): RippleBackground(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null)

    init {
        startRippleAnimation()
    }
}