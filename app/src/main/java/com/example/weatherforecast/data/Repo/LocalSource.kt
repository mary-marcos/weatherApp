package com.example.weatherforecast.data.Repo

import android.content.Context
import com.example.weatherforecast.data.localData.AlarmDao
import com.example.weatherforecast.data.localData.CurrentWeatherDao
import com.example.weatherforecast.data.localData.FavDataBase
import com.example.weatherforecast.data.localData.FavDao
import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.HourlyWeather
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ILocalSource {
    fun getCurrentWeather(): Flow<List<CurrentWeatherDataEntity>>
    suspend fun deleteCurrentWeather(): Int
    suspend fun setCurrentWeather(current: CurrentWeatherDataEntity): Long

    /////
        suspend fun insertOnerAlarm(alarm: WeatherAlarm): Long

    suspend fun deleteAlarm(alarm: WeatherAlarm): Int

    suspend  fun deleteAlarmById(alarmId: Int): Int
    suspend fun updateIsActiveStatus(alarm: WeatherAlarm, isactive: Boolean): Int
    fun getStoredAlarms(): Flow<List<WeatherAlarm>>

    ////
   suspend fun insertFav(fav: FavItem): Long

    suspend fun deleteFav(fav: FavItem): Int

    suspend fun getStoredLocation(): Flow<List<FavItem>>

    // Get daily weather data as a Flow
   fun getDailyWeather(): Flow<List<DailyWeather>>

    // Get hourly weather data as a Flow
   fun getHourlyWeather(): Flow<List<HourlyWeather>>

    // Insert hourly weather data into the database
   suspend fun setAllHourlyWeather(hourly: List<HourlyWeather>): List<Long>

    // Insert daily weather data into the database
   suspend fun setAllDailyWeather(daily: List<DailyWeather>): List<Long>

    // Delete all daily weather data
   suspend fun deleteAllDailyWeather(): Int

    // Delete all hourly weather data
   suspend fun deleteAllHourlyWeather(): Int
}

class LocalSource (private val myfavDao: FavDao, private val alarmDao: AlarmDao, private val currentao:CurrentWeatherDao) :
    ILocalSource {

    companion object {
        @Volatile
        private var INSTANCE: LocalSource? = null

        fun getInstance(context: Context): LocalSource {
            return INSTANCE ?: synchronized(this) {
                val Fdao = FavDataBase.getInstance(context).favDao()
                val AlarDao = FavDataBase.getInstance(context).AlarmDao()
                val currendao = FavDataBase.getInstance(context).currentWeatherDao()
                val instance = LocalSource(Fdao,AlarDao,currendao)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun getCurrentWeather(): Flow<List<CurrentWeatherDataEntity>>{
        return currentao.getCurrentWeather()
    }

   override suspend fun deleteCurrentWeather():Int{
        return currentao.deleteCurrentWeather()
    }
    override suspend fun setCurrentWeather(current:CurrentWeatherDataEntity):Long{
        return currentao.setCurrentWeather(current)
    }
/////
    override suspend fun insertOnerAlarm(alarm: WeatherAlarm):Long {
        var result= alarmDao.setOneAlarm(alarm)
        return result
    }

    override suspend fun deleteAlarm(alarm: WeatherAlarm):Int {
        return   alarmDao.deleteOneAlarm(alarm)
    }

    override suspend fun deleteAlarmById(alarmId: Int): Int {
      return 1
    }

    override suspend fun updateIsActiveStatus(alarm: WeatherAlarm, isactive: Boolean): Int {
        return 1
    }

    //    override suspend  fun deleteAlarmById(alarmId:Int):Int{
//        return   alarmDao.deleteAlarmById(alarmId)
//    }
//    override suspend fun updateIsActiveStatus(alarm: WeatherAlarm, isactive: Boolean):Int {
//        return   alarmDao.updateIsActiveStatus(alarm.id,isactive)
//    }
     override fun getStoredAlarms(): Flow<List<WeatherAlarm>> {

        return alarmDao.getAllAlarms()
    }
    ////
    override suspend fun insertFav(fav: FavItem):Long {
        var result= myfavDao.setOneFav(fav)
        return result
    }

    override suspend fun deleteFav(fav: FavItem):Int {
     return   myfavDao.deleteOneFav(fav)
    }

    override suspend fun getStoredLocation(): Flow<List<FavItem>> {
//        return flow {val result= apiService.getCurrentWeather(lat, lon, apiKey, lang)
//            emit(result)}

        return myfavDao.getAllFav()
    }

    /////////

    // Get daily weather data as a Flow
    override fun getDailyWeather(): Flow<List<DailyWeather>> {
        return currentao.getDailyWeather()
    }

    // Get hourly weather data as a Flow
    override fun getHourlyWeather(): Flow<List<HourlyWeather>> {
        return currentao.getHourlyWeather()
    }

    // Insert hourly weather data into the database
    override suspend fun setAllHourlyWeather(hourly: List<HourlyWeather>):List<Long> {
     return   currentao.setAllHourlyWeather(*hourly.toTypedArray())
    }

    // Insert daily weather data into the database
    override suspend fun setAllDailyWeather(daily: List<DailyWeather>): List<Long> {
        return      currentao.setAllDailyWeather(*daily.toTypedArray())

    }

    // Delete all daily weather data
    override suspend fun deleteAllDailyWeather():Int {
        var k=  currentao.deleteAllDailyList()
        return  k
    }

    // Delete all hourly weather data
    override suspend fun deleteAllHourlyWeather():Int {
        var k=currentao.deleteAllHourlyList()
        return  k
    }
}