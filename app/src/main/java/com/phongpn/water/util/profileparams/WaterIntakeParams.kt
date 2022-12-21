package com.phongpn.water.util.profileparams

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.constant.params.OZ_UK
import com.phongpn.water.util.constant.params.OZ_US
import com.phongpn.water.util.toLbs
import com.phongpn.water.util.toMl
import com.phongpn.water.util.toOz_Uk
import com.phongpn.water.util.toOz_Us
import java.lang.reflect.Modifier

/**
 *     class hold data like sex, activity, ... affect to amount waer a day
 */
class WaterIntakeParams: BaseProfileParamsObserve(), SaveToSharePreference {
    companion object {
        const val AMOUNT = "amount"
        private const val DATA_WATER_INFO = "data_water_info"

        const val RESULT_CALCULATE = "calculate"

        /**
         * Less 1 hour
         */
        const val LIGHT_ACTIVITY = "Light"

        /**
         * More than 1 hour
         */
        const val ACTIVE = "Active"

        /**
         * Manual labor
         */
        const val VERY_ACTIVE = "Very active"

        // weather instant
        const val COLD = "Cold"
        const val TEMPERATE = "Temperate"
        const val HOT = "Hot"
        const val MALE = "MALE"
        const val FEMALE = "FEMALE"
        const val SEDENTARY = "Sedentary"


        private var instance = WaterIntakeParams()
        fun getInstance() = instance

    }
    override fun save(context: Context){
        val jsonWater = Gson().toJson(instance)
        context.getSharedPreferences(DATA_WATER_INFO, Context.MODE_PRIVATE).edit {
            putString(DATA_WATER_INFO, jsonWater)
        }
    }

    override fun get(context: Context){
        context.getSharedPreferences(DATA_WATER_INFO, Context.MODE_PRIVATE).getString(DATA_WATER_INFO, null)?.let {
            Gson().fromJson(it, WaterIntakeParams::class.java).let { instance = it }
        }
    }


    var sex: String = MALE
        set(value) {
            field = value
            calculate()
        }
    var pregnant: Boolean? = null
        set(value) {
            field = value
            calculate()
        }
    var breastfeeding: Boolean? = null
        set(value) {
            field = value
            calculate()
        }
    var weight: Int = 60
        set(value) {
            field = value
            calculate()
        }

    var activity: String = LIGHT_ACTIVITY
        set(value) {
            field = value
            calculate()
        }
    var weather: String = TEMPERATE
        set(value) {
            field = value
            calculate()
        }
    var hotDay = false
        set(value) {
            field = value
            calculate()
        }

    var activeDay = false
        set(value) {
            field = value
            calculate()
        }

    var amount = 2300
        set(value) {
            if (field != value){
                field = value
                notifyDataChanged(AMOUNT, value)
            }
        }
    /**
     * Calculate amount of water should be drink a day. Note that result have unit is ML
     */
    private fun calculate() {
        var amount = 0
        amount = weight.toLbs(UnitParams.getInstance().unitWeight) / 2
        amount = amount.toMl(OZ_US)
        //round amount, like 2190 ->2200 or 2102 -> 2100
        amount.apply {
            var a = amount / 100
            if (amount % 100 >= 50) a++
            amount = a * 100
        }
        if (sex == FEMALE) {
            amount -= 100
            if (pregnant == true) amount += 600
            if (breastfeeding == true) amount += 800
        }
        when (activity) {
            LIGHT_ACTIVITY -> amount += 100
            ACTIVE -> amount += 400
            VERY_ACTIVE -> amount += 800
        }
        when (weather) {
            TEMPERATE -> amount += 300
            HOT -> amount += 700
        }
        if (hotDay) amount += 500

        if (activeDay) amount += 500

        this.amount = amount
    }

}