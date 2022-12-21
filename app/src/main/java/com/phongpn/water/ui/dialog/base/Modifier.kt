package com.phongpn.water.ui.dialog.base

class Modifier(set: (Modifier.() -> Unit)? = null) {
    constructor() : this(null)
    init {
        set?.invoke(this)
    }
    //left, top, right, bottom
    var mPadding : List<Int>? = null

    //left, top, right, bottom
    var mMargin : List<Int>? = null

    //top-left, top-right, bottom-left, bottom-right
    var mCorner : List<Int>? = null

    fun padding(all: Int){
        mPadding = listOf(all,all,all,all)
    }
    fun padding(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0){
        mPadding = listOf(left, top, right, bottom)
    }

    fun margin(all: Int){
        mMargin = listOf(all, all, all, all)
    }
    fun margin(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0){
        mMargin = listOf(left, top, right, bottom)
    }

    fun corner(all: Int){
        mCorner = listOf(all,all,all,all)
    }

    fun corner(topLeft: Int = 0, topRight: Int = 0, bottomLeft: Int = 0, bottomRight: Int = 0){
        mCorner = listOf(topLeft, topRight, bottomLeft, bottomRight)
    }


}