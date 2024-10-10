package com.example.weatherforecast.data.localData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Query("SELECT * FROM alarm_table")
    fun getAllAlarms(): Flow<List<WeatherAlarm>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllAlarm(vararg alarm: WeatherAlarm): List<Long>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setOneAlarm(alarm: WeatherAlarm): Long


    @Delete
    suspend fun deleteOneAlarm(alarm: WeatherAlarm): Int

    @Query("DELETE FROM alarm_table WHERE duration = :alarmId")
    suspend fun deleteAlarmById(alarmId: Long): Int

    @Query("UPDATE alarm_table SET isActive = :isActive WHERE duration = :alarmId")
    suspend fun updateIsActiveStatus(alarmId: Long, isActive: Boolean): Int
}