package com.example.weatherforecast.data.Repo

import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.ForecastWeatherResponse
import kotlinx.coroutines.flow.Flow

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


class Repos (private val weatherRemoteSource: WeatherRemoteSourceImp,private val localDB: LocalSource)
{


    companion object {
        // Singleton instance
        @Volatile
        private var INSTANCE: Repos? = null
        fun getInstance(  weatherRemoteSource: WeatherRemoteSourceImp,localDB:LocalSource): Repos {

            return INSTANCE ?: synchronized(this) {
                val instance = Repos(weatherRemoteSource,localDB)

                INSTANCE = instance
                instance
            }
        }
    }



    suspend fun  getCurrentWeather(lat: Double,lon: Double, lang: String):Flow<CurrenWeather>
    {
        val weatherResponse = weatherRemoteSource.getCurrentWeather(lat, lon, lang)

        return weatherResponse
    }

    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ForecastWeatherResponse> {

            return weatherRemoteSource.getForecastWeather(lat, lon,lang)
    }

    suspend fun insertFav(fav: FavItem):Long{
          var rs= localDB.insertFav(fav)
        return  rs
    }
    suspend fun deleteFav(fav: FavItem):Int{
        var rs= localDB.deleteFav(fav)
        return  rs
    }

    suspend fun getFavorateLocations(): Flow<List<FavItem>>{
        var rs= localDB.getStoredLocation()
    return rs
    }

}