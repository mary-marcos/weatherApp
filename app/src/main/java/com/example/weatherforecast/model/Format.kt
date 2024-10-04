package com.example.weatherforecast.model

fun convertKelvinToFahrenheit(temp: Double):String{
    return String.format("%.2f",((temp - 273.15) * 9/5 + 32))
}
fun convertKelvinToCelsius(temp:Double):String{
    return temp.minus(273.15).toInt().toString()
}

//
