package com.example.weatherforecast.data.Repo

import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.ForecastWeatherResponse
import com.example.weatherforecast.model.HourlyWeather
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

//
//class Repos (private val weatherRemoteSource: RemoteSource){
//  suspend fun  getCurrentWeather(lat: Double,lon: Double, lang: String): CurrenWeather
//  {
//
//      val weatherData = weatherRemoteSource.getWeatherForecast(lat, lon, lang)
//      return weatherData
//    }
//
//}


interface IRepos {
    suspend fun  getCurrentWeather(lat: Double, lon: Double, lang: String): Flow<CurrenWeather>

    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ForecastWeatherResponse>

    suspend fun insertFav(fav: FavItem): Long
    suspend fun deleteFav(fav: FavItem): Int
    suspend fun getFavorateLocations(): Flow<List<FavItem>>
    suspend fun insertOnerAlarm(alarm: WeatherAlarm): Long

    suspend fun deleteAlarm(alarm: WeatherAlarm): Int

    suspend fun updateIsActiveStatus(alarm: WeatherAlarm, isactive: Boolean): Int
    fun getStoredAlarms(): Flow<List<WeatherAlarm>>

    suspend  fun deleteAlarmById(alarmId: Int): Int
    fun getCurrentWeatherlocal(): Flow<List<CurrentWeatherDataEntity>>
    suspend fun deleteCurrentWeather(): Int
    suspend fun setCurrentWeather(current: CurrentWeatherDataEntity): Long

    /////
        fun getDailyWeather(): Flow<List<DailyWeather>>

    // Get hourly weather data as a Flow
        fun getHourlyWeather(): Flow<List<HourlyWeather>>

    // Insert hourly weather data into the database
        suspend fun setAllHourlyWeather(hourly: List<HourlyWeather>): List<Long>

    // Insert daily weather data into the database
        suspend fun setAllDailyWeather(daily: List<DailyWeather>) : List<Long>

    // Delete all daily weather data
    suspend fun deleteAllDailyWeather(): Int

    // Delete all hourly weather data
    suspend fun deleteAllHourlyWeather(): Int
}

class Repos (private val weatherRemoteSource: IWeatherRemoteSourceImp, private val localDB: ILocalSource) :
    IRepos {


    companion object {
        // Singleton instance
        @Volatile
        private var INSTANCE: Repos? = null
        fun getInstance(  weatherRemoteSource: IWeatherRemoteSourceImp,localDB:ILocalSource): Repos {

            return INSTANCE ?: synchronized(this) {
                val instance = Repos(weatherRemoteSource,localDB)

                INSTANCE = instance
                instance
            }
        }
    }



    override suspend fun  getCurrentWeather(lat: Double, lon: Double, lang: String):Flow<CurrenWeather>
    {
       // val weatherResponse = weatherRemoteSource.getCurrentWeather(lat, lon, lang)
        return flowOf(weatherRemoteSource.getCurrentWeather(lat, lon, lang))

    }



    override suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ForecastWeatherResponse> {
        return flowOf(weatherRemoteSource.getForecastWeather(lat, lon,lang))

    }

    override suspend fun insertFav(fav: FavItem):Long{
          var rs= localDB.insertFav(fav)
        return  rs
    }
    override suspend fun deleteFav(fav: FavItem):Int{
        var rs= localDB.deleteFav(fav)
        return  rs
    }

    override suspend fun getFavorateLocations(): Flow<List<FavItem>>{
        var rs= localDB.getStoredLocation()
    return rs
    }


    override suspend fun insertOnerAlarm(alarm: WeatherAlarm):Long {
        var result= localDB.insertOnerAlarm(alarm)
        return result
    }

    override suspend fun deleteAlarm(alarm: WeatherAlarm):Int {
        return   localDB.deleteAlarm(alarm)
    }
    override suspend fun updateIsActiveStatus(alarm: WeatherAlarm, isactive: Boolean):Int {
        return   localDB.updateIsActiveStatus(alarm,isactive)
    }
    override fun getStoredAlarms(): Flow<List<WeatherAlarm>> {

        return localDB.getStoredAlarms()
    }

    override suspend  fun deleteAlarmById(alarmId:Int):Int{
        return   localDB.deleteAlarmById(alarmId)
    }

    override fun getCurrentWeatherlocal(): Flow<List<CurrentWeatherDataEntity>>{
        return localDB.getCurrentWeather()
    }

    override suspend fun deleteCurrentWeather():Int{
        return localDB.deleteCurrentWeather()
    }
    override suspend fun setCurrentWeather(current:CurrentWeatherDataEntity):Long{
        return localDB.setCurrentWeather(current)
    }

/////
override fun getDailyWeather(): Flow<List<DailyWeather>> {
        return localDB.getDailyWeather()
    }

    // Get hourly weather data as a Flow
    override fun getHourlyWeather(): Flow<List<HourlyWeather>> {
        return localDB.getHourlyWeather()
    }

    // Insert hourly weather data into the database
    override suspend fun setAllHourlyWeather(hourly: List<HourlyWeather>):List<Long> {
       return localDB.setAllHourlyWeather(hourly)
    }

    // Insert daily weather data into the database
    override suspend fun setAllDailyWeather(daily: List<DailyWeather>) :List<Long>{
      return  localDB.setAllDailyWeather(daily)
    }

    // Delete all daily weather data
    override suspend fun deleteAllDailyWeather():Int {
      return  localDB.deleteAllDailyWeather()
    }

    // Delete all hourly weather data
    override suspend fun deleteAllHourlyWeather():Int {
        return  localDB.deleteAllHourlyWeather()
    }

}