package com.example.weatherforecast.data.Repo

import com.example.weatherforecast.data.Api.ApiServices

import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.ForecastWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//interface RemoteSource {
//
//    suspend fun getWeatherForecast(lat:Double,lon:Double,lang:String): CurrenWeather
//
//
//}

interface IWeatherRemoteSourceImp {
    val apiKey: String

    suspend fun getCurrentWeather(lat: Double, lon: Double, lang: String): CurrenWeather

    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): ForecastWeatherResponse
}

class WeatherRemoteSourceImp(private val apiService: ApiServices) : IWeatherRemoteSourceImp {
    override val apiKey = "fafa4312e27b3dc30f42d0b6d3eccabf"

    companion object {
        @Volatile
        private var INSTANCE: WeatherRemoteSourceImp? = null
        fun getInstance(): WeatherRemoteSourceImp {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherRemoteSourceImp(RetrofitHelper.apiServiceInstance)
                INSTANCE = instance
                instance
            }
        }
    }


    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang: String): CurrenWeather {
        ///////////// later correct
     //   return flow {val result= apiService.getCurrentWeather(lat, lon, apiKey, lang)
       //     emit(result)
     //   }

        return apiService.getCurrentWeather(lat, lon, apiKey, lang)
    }

    override suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): ForecastWeatherResponse {

      return  apiService.getForecastWeather(lat, lon, apiKey, lang)
        }

}

object RetrofitHelper {
    private const val BASE_URL = "https://api.openweathermap.org/"
    var retrofit = Retrofit.Builder()

        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiServiceInstance = retrofit.create(ApiServices::class.java)

}