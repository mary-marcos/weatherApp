package com.example.weatherforecast.data.Repo

import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.ForecastWeatherResponse

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


class Repos (private val weatherRemoteSource: WeatherRemoteSourceImp)
{


    companion object {
        // Singleton instance
        @Volatile
        private var INSTANCE: Repos? = null
        fun getInstance(  weatherRemoteSource: WeatherRemoteSourceImp): Repos {

            return INSTANCE ?: synchronized(this) {
                val instance = Repos(weatherRemoteSource)

                INSTANCE = instance
                instance
            }
        }
    }



    suspend fun  getCurrentWeather(lat: Double,lon: Double, lang: String): CurrenWeather
    {
        val weatherData = weatherRemoteSource.getCurrentWeather(lat, lon, lang)
        return weatherData
    }

    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): ForecastWeatherResponse {

            return weatherRemoteSource.getForecastWeather(lat, lon,lang)
    }

}