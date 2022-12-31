package com.phongpn.water.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phongpn.water.storage.SharePrefUtil
import com.phongpn.water.util.constant.params.OZ_US
import com.phongpn.water.util.profileparams.AppSetting
import com.phongpn.water.util.profileparams.UnitParams
import com.phongpn.water.util.profileparams.WaterIntakeParams
import com.phongpn.water.util.toLbs
import com.phongpn.water.util.toMl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        calculate()
    }

    val pregnant = MutableLiveData(SharePrefUtil.pregnant)
    fun setPregnant(pregnant: Boolean) {
        this.pregnant.postValue(pregnant)
        SharePrefUtil.pregnant = pregnant
        calculate()
    }

    val breastfeeding = MutableLiveData(SharePrefUtil.breastfeeding)
    fun setBreastFeeding(breastfeeding: Boolean) {
        this.breastfeeding.postValue(breastfeeding)
        SharePrefUtil.breastfeeding = breastfeeding
        calculate()
    }

    val weight = MutableLiveData(SharePrefUtil.weight)
    fun setWeight(weight: Int) {
        this.weight.postValue(weight)
        SharePrefUtil.weight = weight
        calculate()
    }

    val activity = MutableLiveData(SharePrefUtil.activity)
    fun setActivity(activity: String) {
        this.activity.postValue(activity)
        SharePrefUtil.activity = activity
        calculate()
    }

    val weather = MutableLiveData(SharePrefUtil.weather)
    fun setWeather(weather: String) {
        this.weather.postValue(weather)
        SharePrefUtil.weather = weather
        calculate()
    }

    val goal = MutableLiveData(SharePrefUtil.goal)
    fun setGoal(amount: Int) {
        this.goal.postValue(amount)
        SharePrefUtil.goal = amount
    }

    private fun calculate() {
        var goal = 0
        goal = SharePrefUtil.weight.toLbs(unitWeight.value!!) / 2
        goal = goal.toMl(OZ_US)
        //round amount, like 2190 ->2200 or 2102 -> 2100
        goal.apply {
            var a = goal / 100
            if (goal % 100 >= 50) a++
            goal = a * 100
        }
        if (SharePrefUtil.sex == WaterIntakeParams.FEMALE) {
            goal -= 100
            if (SharePrefUtil.pregnant) goal += 600
            if (SharePrefUtil.breastfeeding) goal += 800
        }
        when (SharePrefUtil.activity) {
            WaterIntakeParams.LIGHT_ACTIVITY -> goal += 100
            WaterIntakeParams.ACTIVE -> goal += 400
            WaterIntakeParams.VERY_ACTIVE -> goal += 800
        }
        when (SharePrefUtil.weather) {
            WaterIntakeParams.TEMPERATE -> goal += 300
            WaterIntakeParams.HOT -> goal += 700
        }
        setGoal(goal)
    }
}