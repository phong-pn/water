package com.phongpn.water.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.phongpn.water.adapter.ListDrinkIconAdapter
import com.phongpn.water.adapter.ListDrinkIconAdapter.*

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    val totalDrinkToday = MutableLiveData<Int>()
    val currentTotalDrinkToday = MutableLiveData<Int>()
    val unitDrink = MutableLiveData<String>()
    val isHotDay = MutableLiveData(false)
    val isActivateDay = MutableLiveData(false)
    var shortcutIcon = MutableLiveData(arrayListOf<DrinkIconData>())

    init {
        getShortcutIcon()
    }

    fun addShortcutIcon(data : DrinkIconData) {
        shortcutIcon.apply {
            if (value!!.size == 0) {
                value!!.add(data)
                postValue(value)
            }
            else {
                value!!.forEachIndexed { index, drinkIconData ->
                        if (drinkIconData.type == data.type) {
                            value!![index] = data
                            shortcutIcon.postValue(value)
                            return
                        }

                }
                postValue(value!!.apply {
                    if (size == 4) removeAt(0)
                    add(data) })
            }

        }
    }
    private fun getShortcutIcon(){
        val type = object : TypeToken<ArrayList<DrinkIconData>>(){}.type
        val listShortcutIcon = getApplication<Application>().getSharedPreferences("shortcut", Context.MODE_PRIVATE)
            .getString("shortcut", null)
            ?.let { Gson().fromJson<ArrayList<DrinkIconData>>(it, type) }
            ?.let { shortcutIcon.postValue(it) }

    }

    fun saveShortcutIcon(context: Context){
        context.getSharedPreferences("shortcut", Context.MODE_PRIVATE)
            .edit().apply {
                putString("shortcut", Gson().toJson(shortcutIcon.value))
                apply()
            }
    }
}