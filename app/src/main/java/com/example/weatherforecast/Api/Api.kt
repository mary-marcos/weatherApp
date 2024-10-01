package com.example.weatherforecast.Api

import com.example.weatherforecast.model.CurrenWeather
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
    ): Response<CurrenWeather>
}
object RetrofitHelper{
    private const val BASE_URL = "https://api.openweathermap.org/"
    var retrofit = Retrofit.Builder()

        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiInstance = retrofit.create(ApiServices::class.java)
}