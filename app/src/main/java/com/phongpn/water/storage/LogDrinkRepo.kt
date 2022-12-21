package com.phongpn.water.storage

import android.content.Context
import android.util.Log
import androidx.lifecycle.map
import com.phongpn.water.entity.LogDaily
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.util.Drink
import java.util.*

object LogDrinkRepo{
    private lateinit var dao : LogDrinkDao
    fun initDao(context: Context){
        dao =  LogDrinkDatabase.getInstance(context).getDao()
    }
    fun update(log : LogDrink) = dao.update(log.apply {
        idDate = createIdDate()
    })

    fun delete(log: LogDrink) = dao.delete(log)

    //make sure idDate is correct before add or update to db
    fun insert(log : LogDrink) = dao.insert(log.apply { idDate = createIdDate() })

    fun allLog()  = dao.allLog()

    fun log(id : Int) = dao.log(id)

    fun totalAll() = dao.allLog().map {
        val listLogDaily = it.map { it.toLogDaily() }
        groupByDay(listLogDaily)
    }
    private fun LogDrink.toLogDaily() : LogDaily{
        return   LogDaily(idDate, cal, when(type){
            Drink.WATER -> amount
            Drink.COFFEE, Drink.TEA -> (amount * 0.99f).toInt()
            Drink.PLANT_MILK, Drink.SOFT_DRINK, Drink.MILK, Drink.JUICE -> (amount * 0.9f).toInt()
            Drink.BEER -> (- amount * 1.56f).toInt()
            Drink.WINE -> (- amount * 0.6f).toInt()
            else -> (- amount * 4.8f).toInt()
        })
    }

   private fun groupByDay(list : List<LogDaily>) : List<LogDaily> {
       val newList = mutableListOf<LogDaily>()
       var logADay = LogDaily("", null, 0)
       for (index in list.indices) {
           if (logADay.idDate == "") {
               logADay = list[index]
           } else {
               if (logADay.idDate == list[index].idDate) {
                   logADay.total = logADay.total?.plus(list[index].total!!)
               } else {
                   newList.add(logADay)
                   logADay = list[index]
               }
           }
           if (index != list.size - 1) continue else newList.add(logADay)
       }
       if (newList.size > 1){
           var checkDayIndex = 0
           val maxDay = Calendar.getInstance().apply { timeInMillis = newList[checkDayIndex].cal!!.timeInMillis }
           while (LogDrink.createIdDate(maxDay) != newList.last().idDate){
               checkDayIndex++
               maxDay.apply {
                   timeInMillis -= 24 * 60 * 60 * 1000
                   val mIdDate = LogDrink.createIdDate(maxDay)
                   if (newList[checkDayIndex].idDate != mIdDate) newList.add(checkDayIndex,
                       LogDaily(mIdDate, Calendar.getInstance().also { it.timeInMillis = timeInMillis }, 0))
               }
           }
       }

       newList.forEach {
           Log.d("ahihi", "${it.idDate} ${it.total}" )

       }
       return newList
   }

    fun deleteAllLogs() {
        dao.deleteAllLogs()
    }

    fun totalAmountByIdDate(idDate : String): Int{
        var amount = 0
        for (log in dao.allLgByIddDate(idDate).map { it.toLogDaily() }) amount += log.total!!
        return amount
    }
}