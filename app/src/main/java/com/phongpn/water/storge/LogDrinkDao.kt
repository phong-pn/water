package com.phongpn.water.storge

import androidx.lifecycle.LiveData
import androidx.room.*
import com.phongpn.water.entity.LogDaily
import com.phongpn.water.entity.LogDrink

@Dao
interface LogDrinkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(log : LogDrink)

    @Update
    fun update(log : LogDrink)

    @Delete
    fun delete(log: LogDrink)

    /**
     * Get all logs
     */
    @Query("SELECT * FROM log_drink ORDER BY cal DESC")
    fun allLog() : LiveData<List<LogDrink>>

    @Query("SELECT * FROM log_drink WHERE idDate = :idDate ORDER BY cal DESC")
    fun allLgByIddDate(idDate : String) : List<LogDrink>

    /**
     * Get log with id
     * @param id id of log
     * @return log that has id
     */
    @Query("SELECT * FROM log_drink WHERE id = :id")
    fun log(id : Int) : LogDrink

    @Query("DELETE FROM log_drink")
    fun deleteAllLogs()




}