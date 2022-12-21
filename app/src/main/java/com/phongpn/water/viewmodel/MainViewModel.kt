package com.phongpn.water.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phongpn.water.util.constant.params.OZ_US
import com.phongpn.water.util.profileparams.AppSetting
import com.phongpn.water.util.profileparams.UnitParams
import com.phongpn.water.util.profileparams.WaterIntakeParams
import com.phongpn.water.util.toLbs
import com.phongpn.water.util.toMl

class MainViewModel : ViewModel() {
    val appSetting = MutableLiveData(AppSetting())

    fun setSound(sound: Boolean) {
        val value = appSetting.value
        value?.sound = sound
        appSetting.postValue(value)
    }

    val unitParams = MutableLiveData(UnitParams())

    fun setUnitDrink(unit: String) {
        val value = unitParams.value
        value?.unitDrink = unit
        unitParams.postValue(value)
    }

    fun setUnitWeight(unit: String) {
        val value = unitParams.value
        value?.unitWeight = unit
        unitParams.postValue(value)
    }


    val waterIntakeParams = MutableLiveData(WaterIntakeParams())

    fun setSex(sex: String) {
        val value = waterIntakeParams.value
        value?.sex = sex
        waterIntakeParams.postValue(value)
    }

    fun setPregnant(pregnant: Boolean) {
        val value = waterIntakeParams.value
        value?.pregnant = pregnant
        waterIntakeParams.postValue(value)
    }

    fun setBreastFeeding(breastfeeding: Boolean) {
        val value = waterIntakeParams.value
        value?.breastfeeding = breastfeeding
        waterIntakeParams.postValue(value)
    }

    fun setWeight(weight: Int) {
        val value = waterIntakeParams.value
        value?.weight = weight
        waterIntakeParams.postValue(value)
    }

    fun setActivity(activity: String) {
        val value = waterIntakeParams.value
        value?.activity = activity
        waterIntakeParams.postValue(value)
    }


    fun setWeather(weather: String) {
        val value = waterIntakeParams.value
        value?.weather = weather
        waterIntakeParams.postValue(value)
    }


    fun setAmount(amount: Int) {
        val value = waterIntakeParams.value
        value?.amount = amount
        waterIntakeParams.postValue(value)
    }

    private fun calculate() {
        waterIntakeParams.value?.apply {
            var amount = 0
            amount = weight.toLbs(UnitParams.getInstance().unitWeight) / 2
            amount = amount.toMl(OZ_US)
            //round amount, like 2190 ->2200 or 2102 -> 2100
            amount.apply {
                var a = amount / 100
                if (amount % 100 >= 50) a++
                amount = a * 100
            }
            if (sex == WaterIntakeParams.FEMALE) {
                amount -= 100
                if (pregnant == true) amount += 600
                if (breastfeeding == true) amount += 800
            }
            when (activity) {
                WaterIntakeParams.LIGHT_ACTIVITY -> amount += 100
                WaterIntakeParams.ACTIVE -> amount += 400
                WaterIntakeParams.VERY_ACTIVE -> amount += 800
            }
            when (weather) {
                WaterIntakeParams.TEMPERATE -> amount += 300
                WaterIntakeParams.HOT -> amount += 700
            }
            if (hotDay) amount += 500

            if (activeDay) amount += 500

            this.amount = amount
        }
    }
}