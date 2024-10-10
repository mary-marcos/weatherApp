package com.example.weatherforecast.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DailyWeather_table")
data class DailyWeather(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val date: String,
    val day: String,
    val minmaxTemp: String,
    val iconImg: String
)
@Entity(tableName = "HourlyWeather_table")
data class HourlyWeather(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val hour: String,
    val Temp: String,
    val iconImg: String
)