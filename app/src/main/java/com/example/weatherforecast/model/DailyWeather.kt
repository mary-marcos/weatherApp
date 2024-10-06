package com.example.weatherforecast.model

data class DailyWeather(
    val date: String,      // Date in the format YYYY-MM-DD
    val day: String,      // day name
      // Minimum temperature
    val minmaxTemp: String,   // Maximum temperature
    val iconImg: String    // URL for the weather icon image
)

data class HourlyWeather(
    val hour: String,
    val Temp: String,
    val iconImg: String
)