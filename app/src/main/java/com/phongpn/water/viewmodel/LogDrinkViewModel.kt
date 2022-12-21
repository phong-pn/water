package com.phongpn.water.viewmodel

import androidx.lifecycle.*
import com.phongpn.water.entity.LogDaily
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.storge.LogDrinkRepo
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.toMl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogDrinkViewModel : ViewModel() {
    var allLog : LiveData<List<LogDrink>> = MutableLiveData()
    var totalAll : LiveData<List<LogDaily>> = MutableLiveData()
    fun getLogs(){
        viewModelScope.launch {
           allLog = LogDrinkRepo.allLog()
        }
    }
    init {
        getTotalALl()
    }

    fun log(id : Int) : LogDrink{
        return LogDrinkRepo.log(id)
    }

    fun insert(log : LogDrink){
        viewModelScope.launch {
            //save log with unit ml, so here change unit to ml before insert
            log.apply {
                amount = amount.toMl( ML)
                LogDrinkRepo.insert(this)
            }
        }
    }

    fun update(log: LogDrink){
        viewModelScope.launch {
            //save log with unit ml, so here change unit to ml before update
            log.apply {
                amount = amount.toMl( ML)
                LogDrinkRepo.update(this)
            }
        }
    }

    fun delete(log: LogDrink){
        viewModelScope.launch {
            LogDrinkRepo.delete(log)
        }
    }

    fun deleteALlLogs(){
        viewModelScope.launch {
            LogDrinkRepo.deleteAllLogs()
        }
    }


    private fun getTotalALl(){
        viewModelScope.launch {
            totalAll = LogDrinkRepo.totalAll()
        }
    }
}