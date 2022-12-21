package com.phongpn.water.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "log_drink")
data class LogDrink (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    var cal : Calendar,
    var type : String,
    var amount : Int)
{
    var idDate = ""
    @Ignore
    constructor(amount: Int, type: String, cal: Calendar) : this(id = 0,cal = cal, type = type, amount = amount)
    fun createIdDate() = "${cal[Calendar.YEAR]}/${cal[Calendar.MONTH]}/${cal[Calendar.DAY_OF_MONTH]}"

    init {
        idDate = createIdDate()
    }
    companion object{
        fun createIdDate(cal: Calendar) = "${cal[Calendar.YEAR]}/${cal[Calendar.MONTH]}/${cal[Calendar.DAY_OF_MONTH]}"
    }

}