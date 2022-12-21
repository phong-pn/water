package com.phongpn.water.storge

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phongpn.water.entity.LogDrink

@Database(entities = [LogDrink::class], version = 1)
@TypeConverters(CalendarConvert::class)
abstract class LogDrinkDatabase : RoomDatabase() {
    abstract fun getDao() : LogDrinkDao

    companion object{
        const val DB_NAME = "log_drink_db"
        private var instance : LogDrinkDatabase? = null
        fun getInstance(context: Context) : LogDrinkDatabase{
            if (instance == null){
                synchronized(this){
                    if (instance == null){
                        instance = Room.databaseBuilder(context, LogDrinkDatabase::class.java, DB_NAME )
                            .allowMainThreadQueries()
                            .build()
                    }
                }

            }
            return instance!!
        }
    }
}