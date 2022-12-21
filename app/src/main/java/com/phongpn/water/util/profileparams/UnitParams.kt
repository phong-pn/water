package com.phongpn.water.util.profileparams

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.phongpn.water.util.constant.params.KG
import com.phongpn.water.util.constant.params.ML
import java.lang.reflect.Modifier

class UnitParams : BaseProfileParamsObserve(), SaveToSharePreference {
    companion object  {
        private var instance = UnitParams()
        fun getInstance() = instance
        const val UNIT_WEIGHT = "unit_weight"
        const val UNIT_DRINK = "unit_drink"
        private const val UNIT_PARAMS = "Unit params"
    }
    var unitDrink = ML
        set(value) {
            if (field != value){
                field = value
                notifyDataChanged(UNIT_DRINK, field)
            }

        }
    var unitWeight = KG
        set(value) {
            field = value
            notifyDataChanged(UNIT_WEIGHT, field)
        }
    override fun get(context: Context) {
        context.getSharedPreferences(UNIT_PARAMS, Context.MODE_PRIVATE)
            .getString(UNIT_PARAMS, null)
            ?.let {
                Gson().fromJson(it, UnitParams::class.java).apply {
                    instance.unitDrink = unitDrink
                    instance.unitWeight = unitWeight
                }
            }
    }

    override fun save(context: Context) {
        Gson().toJson(instance).apply {
            context.getSharedPreferences(UNIT_PARAMS, Context.MODE_PRIVATE).edit {
                putString(UNIT_PARAMS, this@apply)
            }
        }
    }
}
