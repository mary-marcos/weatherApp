package com.example.weatherforecast.data.Repo

import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.ForecastWeatherResponse
import kotlinx.coroutines.flow.Flow

class FakeRemote(var favList: Flow<List<FavItem>>):IWeatherRemoteSourceImp {
    override val apiKey: String
        get() = TODO("Not yet implemented")

    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang: String): CurrenWeather {
        TODO("Not yet implemented")
    }

    override suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): ForecastWeatherResponse {
        TODO("Not yet implemented")
    }
}