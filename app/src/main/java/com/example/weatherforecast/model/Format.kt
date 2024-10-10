package com.example.weatherforecast.model

import android.content.Context
import android.location.Geocoder
import com.example.weatherforecast.R
import java.text.SimpleDateFormat
import java.util.Locale

fun convertKelvinToFahrenheit(temp: Double):String{
    return String.format("%.2f",((temp - 273.15) * 9/5 + 32))
}
fun convertKelvinToCelsius(temp:Double):String{
    return temp.minus(273.15).toInt().toString()
}


 fun formatDate(dtTxt: String): String
{
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm, EEE, MM/dd/yyyy", Locale.getDefault())
    val date = inputFormat.parse(dtTxt)
    return outputFormat.format(date)
}


fun getweatherIconResourceId(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.day_clear
        "01n" -> R.drawable.nightgood
        "02d" -> R.drawable.day_partial_cloud
        "02n" -> R.drawable.night_cloud_part
        "03d" -> R.drawable.cloudy
        "03n" -> R.drawable.night_cloud_part
        "04d", "04n" -> R.drawable.overcast
        "09d", "09n" -> R.drawable.rain
        "10d" -> R.drawable.day_rain
        "10n" -> R.drawable.rain
        "11d", "11n" -> R.drawable.thinder_rain
        "13d", "13n" -> R.drawable.snow
        "50d", "50n" -> R.drawable.fog
        else -> R.drawable.day_clear
    }}
fun getAddress(lat: Double, lng: Double,context: Context): String {
    val geocoder = Geocoder(context)
    val list = geocoder.getFromLocation(lat, lng, 1)
    if (!list.isNullOrEmpty()) {
        val address = list[0]
        val city = address.adminArea ?: address.subAdminArea ?: "Unknown"
        return city
    }

    return "Unknown"
}

//
