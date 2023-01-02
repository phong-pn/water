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
class WaterIntakeParams {
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

        val listPhysical = listOf(SEDENTARY, LIGHT_ACTIVITY, ACTIVE, VERY_ACTIVE)
        val listClimate = listOf(COLD, TEMPERATE, HOT)
    }
}