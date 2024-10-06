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

class WeatherRemoteSourceImp(private val apiService: ApiServices) {
    val apiKey = "fafa4312e27b3dc30f42d0b6d3eccabf"

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


    suspend fun getCurrentWeather(lat: Double, lon: Double, lang: String): Flow<CurrenWeather> {
        ///////////// later correct
        return flow {val result= apiService.getCurrentWeather(lat, lon, apiKey, lang)
            emit(result)
        }


    }

    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ForecastWeatherResponse> {

        return flow {val result=  apiService.getForecastWeather(lat, lon, apiKey, lang)
            emit(result)
        }
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