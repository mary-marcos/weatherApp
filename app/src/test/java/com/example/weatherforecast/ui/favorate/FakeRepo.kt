package com.example.weatherforecast.ui.favorate

import com.example.weatherforecast.data.Repo.IRepos
import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.ForecastWeatherResponse
import com.example.weatherforecast.model.HourlyWeather
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class FakeRepo:IRepos {
    private var shouldError: Boolean = false
    fun simulateError(shouldError: Boolean) {
        this.shouldError = shouldError
    }

    val alarm= WeatherAlarm(id = 1, hour = 22, minute = 11, type = "not", isActive = false)
    val alarm2= WeatherAlarm(id = 2, hour = 22, minute = 11, type = "not", isActive = false)
    val alarm3= WeatherAlarm(id = 3, hour = 22, minute = 11, type = "not", isActive = false)
    var alarmList= mutableListOf<WeatherAlarm>(alarm, alarm, alarm)

    val fav1=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )
    var fav2=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )
    var fav3=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )
    var fav4=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )


var favList= mutableListOf<FavItem>(fav1, fav2, fav3,fav4)
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<CurrenWeather> {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ForecastWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFav(fav: FavItem): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFav(fav: FavItem): Int {
        if(shouldError){
            throw Exception("Deletion Failed!")
        }
        favList.remove (fav)
        return 1
    }

    override suspend fun getFavorateLocations(): Flow<List<FavItem>> {
        return flow {
            if (shouldError) {
                throw Exception("Fetch error")
            } else {
                emit(favList)
            }
        }
    }

    override suspend fun insertOnerAlarm(alarm: WeatherAlarm): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarm(alarm: WeatherAlarm): Int {
        if(shouldError){
            throw Exception("Deletion Failed!")
        }
        alarmList.remove (alarm)
        return 1
    }

    override suspend fun updateIsActiveStatus(alarm: WeatherAlarm, isactive: Boolean): Int {
        TODO("Not yet implemented")
    }

    override fun getStoredAlarms(): Flow<List<WeatherAlarm>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarmById(alarmId: Int): Int {
//        fo
       return 1
    }

    override fun getCurrentWeatherlocal(): Flow<List<CurrentWeatherDataEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCurrentWeather(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrentWeather(current: CurrentWeatherDataEntity): Long {
        TODO("Not yet implemented")
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