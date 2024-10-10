package com.example.weatherforecast.data.localData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.HourlyWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM current_weather_table")
    fun getCurrentWeather(): Flow<List<CurrentWeatherDataEntity>>





    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setCurrentWeather(currentWeatherDataEntity: CurrentWeatherDataEntity): Long

    @Query("DELETE FROM current_weather_table")
    suspend fun deleteCurrentWeather(): Int

//    @Delete
//    suspend fun deleteCurrentWeather(currentWeatherDataEntity: CurrentWeatherDataEntity): Int

    @Query("SELECT * FROM DailyWeather_table")
    fun getDailyWeather(): Flow<List<DailyWeather>>

    @Query("SELECT * FROM HourlyWeather_table")
    fun getHourlyWeather(): Flow<List<HourlyWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllHourlyWeather(vararg hourly: HourlyWeather): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllDailyWeather(vararg daily: DailyWeather): List<Long>

    @Query("DELETE FROM DailyWeather_table")
    suspend fun deleteAllDailyList(): Int


    @Query("DELETE FROM HourlyWeather_table")
    suspend fun deleteAllHourlyList(): Int
}