package com.phongpn.water.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.phongpn.water.adapter.ListDrinkIconAdapter.DrinkIconData
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.storage.LogDrinkRepo
import com.phongpn.water.storage.SharePrefUtil
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.constant.params.OZ_UK
import com.phongpn.water.util.constant.params.OZ_US
import com.phongpn.water.util.toOz_Uk
import com.phongpn.water.util.toOz_Us
import java.util.*

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var shortcutIcon = MutableLiveData(arrayListOf<DrinkIconData>())

    var mainFragmentUiState: MutableLiveData<MainFragmentUiState>

    init {
        getShortcutIcon()
        mainFragmentUiState = MutableLiveData(
            MainFragmentUiState(
                currentTotalDrinkToday = LogDrinkRepo.totalAmountByIdDate(
                    LogDrink.createIdDate(Calendar.getInstance())
                )
            )
        )
    }

    fun changeTotalDrinkToday(newTotal: Int) {
        mainFragmentUiState.postValue {
            currentTotalDrinkToday = newTotal
        }
    }

    fun addShortcutIcon(data: DrinkIconData) {
        shortcutIcon.apply {
            if (value!!.size == 0) {
                value!!.add(data)
                postValue(value)
            } else {
                value!!.forEachIndexed { index, drinkIconData ->
                    if (drinkIconData.type == data.type) {
                        value!![index] = data
                        shortcutIcon.postValue(value)
                        return
                    }

                }
                postValue(value!!.apply {
                    if (size == 4) removeAt(0)
                    add(data)
                })
            }

        }
    }

    private fun getShortcutIcon() {
        val type = object : TypeToken<ArrayList<DrinkIconData>>() {}.type
        getApplication<Application>().getSharedPreferences("shortcut", Context.MODE_PRIVATE)
            .getString("shortcut", null)
            ?.let { Gson().fromJson<ArrayList<DrinkIconData>>(it, type) }
            ?.let { shortcutIcon.postValue(it) }

    }

    fun saveShortcutIcon(context: Context) {
        context.getSharedPreferences("shortcut", Context.MODE_PRIVATE)
            .edit().apply {
                putString("shortcut", Gson().toJson(shortcutIcon.value))
                apply()
            }
    }

    fun changeGoal(newGoal: Int) {
        mainFragmentUiState.postValue {
            goalToday = newGoal
        }
    }
}

class MainFragmentUiState(
    var currentTotalDrinkToday: Int,
    var goalToday: Int = SharePrefUtil.goal
) {
    val remainToday
        get() = goalToday - currentTotalDrinkToday

    val process: Float
        get() = (currentTotalDrinkToday / goalToday.toFloat()) * 100

    val intakePreview: String
        get()  {
            val mCurrent = currentTotalDrinkToday
            val mTotal = goalToday
            return "$mCurrent ml / $mTotal ml "
        }
}

fun<T> MutableLiveData<T>.postValue(builder: T.() -> Unit) {
    value?.let { builder(it) }
    postValue(value)
}