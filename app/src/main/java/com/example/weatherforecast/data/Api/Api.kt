package com.example.weatherforecast.data.Api

import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.ForecastWeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiServices {


    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("lang") lang:String
    ): CurrenWeather


    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("lang") lang:String
    ): ForecastWeatherResponse
}
