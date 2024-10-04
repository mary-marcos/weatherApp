package com.example.weatherforecast.model


data class ForecastWeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastWeatherData>,
    val city: City
)
data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class ForecastWeatherData(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    // val rain: Rain?,
    val sys: Sys,
    val dt_txt: String
)
