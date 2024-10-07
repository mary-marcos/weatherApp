package com.example.weatherforecast.model

import android.content.Context
import android.location.Geocoder

fun convertKelvinToFahrenheit(temp: Double):String{
    return String.format("%.2f",((temp - 273.15) * 9/5 + 32))
}
fun convertKelvinToCelsius(temp:Double):String{
    return temp.minus(273.15).toInt().toString()
}

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
