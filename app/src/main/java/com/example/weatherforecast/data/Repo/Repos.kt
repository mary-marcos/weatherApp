package com.example.weatherforecast.data.Repo

import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.CurrentWeatherDataEntity
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



    suspend fun  getCurrentWeather(lat: Double,lon: Double, lang: String):Flow<CurrenWeather>
    {
        val weatherResponse = weatherRemoteSource.getCurrentWeather(lat, lon, lang)

//      var  currentweatherdata= CurrentWeatherDataEntity(
//          city = weatherResponse.name,
//          id = weatherResponse.id,
//          description = weatherResponse.weather.get(0).description,
//          iconCode =  weatherResponse.weather.get(0).icon,
//          feelsLike =  weatherResponse.main.feels_like.toString(),
//          temp =   weatherResponse.main.temp.toString(),
//
//          windSpeed =   weatherResponse.wind.speed.toString(),
//          clouds =   weatherResponse.clouds.all.toString(),
//          humidity =   weatherResponse.main.humidity.toString(),
//          pressure =   weatherResponse.main.pressure.toString()
//      )
        return weatherResponse
    }

    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ForecastWeatherResponse> {

            return weatherRemoteSource.getForecastWeather(lat, lon,lang)
    }

}