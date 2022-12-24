package com.phongpn.water.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phongpn.water.storage.SharePrefUtil
import com.phongpn.water.util.constant.params.OZ_US
import com.phongpn.water.util.profileparams.AppSetting
import com.phongpn.water.util.profileparams.UnitParams
import com.phongpn.water.util.profileparams.WaterIntakeParams
import com.phongpn.water.util.toLbs
import com.phongpn.water.util.toMl

class MainViewModel : ViewModel() {
    val appSetting = MutableLiveData(AppSetting())

    val sound = MutableLiveData(SharePrefUtil.sound)
    fun setSound(sound: Boolean) {
        this.sound.postValue(sound)
        SharePrefUtil.sound = sound
    }

    val unitParams = MutableLiveData(UnitParams())

    val unitDrink = MutableLiveData(SharePrefUtil.unitDrink)
    fun setUnitDrink(unit: String) {
        this.unitDrink.postValue(unit)
        SharePrefUtil.unitDrink = unit
    }

    val unitWeight = MutableLiveData(SharePrefUtil.unitWeight)
    fun setUnitWeight(unit: String) {
        this.unitWeight.postValue(unit)
        SharePrefUtil.unitWeight = unit
    }

    val waterIntakeParams = MutableLiveData(WaterIntakeParams())

    val sex = MutableLiveData(SharePrefUtil.sex)
    fun setSex(sex: String) {
        this.sex.postValue(sex)
        SharePrefUtil.sex = sex
    }

    val pregnant = MutableLiveData(SharePrefUtil.pregnant)
    fun setPregnant(pregnant: Boolean) {
        this.pregnant.postValue(pregnant)
        SharePrefUtil.pregnant = pregnant
    }

    val breastfeeding = MutableLiveData(SharePrefUtil.breastfeeding)
    fun setBreastFeeding(breastfeeding: Boolean) {
        this.breastfeeding.postValue(breastfeeding)
        SharePrefUtil.breastfeeding = breastfeeding
    }

    val weight = MutableLiveData(SharePrefUtil.weight)
    fun setWeight(weight: Int) {
        this.weight.postValue(weight)
        SharePrefUtil.weight = weight
    }

    val activity = MutableLiveData(SharePrefUtil.activity)
    fun setActivity(activity: String) {
        this.activity.postValue(activity)
        SharePrefUtil.activity = activity
    }

    val weather = MutableLiveData(SharePrefUtil.weather)
    fun setWeather(weather: String) {
        this.weather.postValue(weather)
        SharePrefUtil.weather = weather
    }

    val amount = MutableLiveData(SharePrefUtil.amount)
    fun setAmount(amount: Int) {
        this.amount.postValue(amount)
        SharePrefUtil.amount = amount
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
            this.amount = amount
            setAmount(amount)
        }
    }
}