package com.phongpn.water.util

import com.phongpn.water.util.constant.params.LBS
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.constant.params.OZ_US
import kotlin.math.roundToInt

fun Int.toLbs(unit : String) : Int{
    return if(unit == LBS) this else (this / 0.453).roundToInt()
}

fun Int.toKg( unit: String):Int{
    return if (unit == LBS)(this * 0.453).roundToInt() else this
}
fun Int.toMl( unit : String) : Int{
    return when(unit){
        ML -> this
        OZ_US -> (this* 29.27).roundToInt()
        else -> (this* 28.41).roundToInt()
    }
}
fun Int.toOz_Uk( unit : String) : Int{
    return when(unit){
        ML -> (this* 0.035).roundToInt()
        OZ_US -> (this* 1.03).roundToInt()
        else -> this
    }
}
fun Int.toOz_Us( unit : String) : Int{
    return when(unit){
        ML -> (this* 0.034).roundToInt()
        OZ_US -> this
        else -> (this* 0.97).roundToInt()
    }
}


