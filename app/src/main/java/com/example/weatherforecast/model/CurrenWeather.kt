package com.example.weatherforecast.model

import android.content.Context
import android.location.Geocoder
import androidx.room.PrimaryKey
import java.io.Serializable


/////////////////////////////////////////

data class CurrentWeatherDataEntity(

    val id: Int = 0,
    var city: String?,
    var temp: String?,
    val description:String?,
    var windSpeed: String?,
    val humidity: String?,
    val feelsLike: String?,
    val pressure: String?,
    val clouds: String?,
    val iconCode: String?,

): Serializable


data class CurrenWeather(
   // @PrimaryKey(autoGenerate = true)
    val id: Int,
    val coord: Coord,

    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,

    var name: String,
    val cod: Int


){
    fun getAddress(lat: Double, lng: Double,context: Context): String {
    val geocoder = Geocoder(context)
    val list = geocoder.getFromLocation(lat, lng, 1)

    if (!list.isNullOrEmpty()) {
        val address = list[0]
        val city = address.adminArea ?: address.subAdminArea ?: "Unknown"
        return city as String
    }

    return "Unknown"
}
}


data class Coord(
    val lon: Double,
    val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    var temp: Double,
    var feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    var humidity: Int,
    val sea_level: Int?,
    val grnd_level: Int?
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double?
)

data class Clouds(
    val all: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)