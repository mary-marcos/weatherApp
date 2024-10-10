package com.example.weatherforecast.data.Repo

import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.HourlyWeather
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


//= MutableStateFlowof<List<FavItem>>()

class FakeLocal(var favList:Flow<List<FavItem>> , var alarmList:Flow<List<WeatherAlarm>>):ILocalSource {



  //  var alarmList= mutableListOf<WeatherAlarm>(alarm, alarm2, alarm3)


    override fun getCurrentWeather(): Flow<List<CurrentWeatherDataEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrentWeather(current: CurrentWeatherDataEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertOnerAlarm(alarm: WeatherAlarm): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarm(alarm: WeatherAlarm): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarmById(alarmId: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updateIsActiveStatus(alarm: WeatherAlarm, isactive: Boolean): Int {
        TODO("Not yet implemented")
    }

    override fun getStoredAlarms(): Flow<List<WeatherAlarm>> {
      return  alarmList
    }

    override suspend fun insertFav(fav: FavItem): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFav(fav: FavItem): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getStoredLocation(): Flow<List<FavItem>> {
        return favList
    }

    override fun getDailyWeather(): Flow<List<DailyWeather>> {
        TODO("Not yet implemented")
    }

    override fun getHourlyWeather(): Flow<List<HourlyWeather>> {
        TODO("Not yet implemented")
    }

    override suspend fun setAllHourlyWeather(hourly: List<HourlyWeather>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun setAllDailyWeather(daily: List<DailyWeather>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllDailyWeather(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllHourlyWeather(): Int {
        TODO("Not yet implemented")
    }
}