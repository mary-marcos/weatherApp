package com.example.weatherforecast.model

data class DailyWeather(
    val date: String,      // Date in the format YYYY-MM-DD
    val day: String,      // day name
    val minTemp: Double,   // Minimum temperature
    val maxTemp: Double,   // Maximum temperature
    val iconImg: String    // URL for the weather icon image
)