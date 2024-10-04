package com.example.weatherforecast.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.example.weatherforecast.model.convertKelvinToFahrenheit
import com.example.weatherforecast.model.convertKelvinToCelsius

import java.util.Locale

class SharePrefrenceData(context :Context) {

    companion object{
        private const val PREF_NAME = "MyPrefs"
        private const val LOCATION_MODE="location_mode"
        private const val LANGUAGE_KEY = "language_key"
        private const val TEMP_UNIT="tempUnit"
        private const val  WIND_SPEED="windSpeed"
        private const val LAT_LNG="latLng"
    }
    private val sharedPreferences=context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )
    private val gson = Gson()



    fun saveLocationMode(mode: String) {
        sharedPreferences.edit().putString(LOCATION_MODE, mode)?.apply()
    }

    fun getLocationMode(): String {
        return sharedPreferences.getString(LOCATION_MODE, "GPS") ?: "GPS"
    }

     fun saveLanguagePreference(context: Context, language: String) {

       // val preferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).apply()

    }

    fun getSavedLanguage(context: Context): String? {
      //  val preferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
      //  Log.d("TAG", "getSavedLanguage:${preferences.getString(LANGUAGE_KEY, Locale.getDefault().language)} ")
        var currentlang=sharedPreferences.getString(LANGUAGE_KEY, Locale.getDefault().language)
        return currentlang
    }


    fun saveLatLng(latLng: LatLng) {
        val json = gson.toJson(latLng)
        sharedPreferences.edit().putString(LAT_LNG, json).apply()
    }

    fun getLatLng(): LatLng? {
        val json = sharedPreferences.getString(LAT_LNG, null)
        return gson.fromJson(json, LatLng::class.java)
    }


    fun setTempunit(temp: String) {
        sharedPreferences.edit().putString(TEMP_UNIT, temp)?.apply()
    }

    fun getTempunit(): String {
        return sharedPreferences.getString(TEMP_UNIT, "KELVIN") ?: "KELVIN"
    }


   fun getFormatedUnit(temp:Double):String{
       return when (getTempunit()) {
           "Celsius" -> convertKelvinToCelsius(temp)+" °C"
           "Fahrenheit" -> convertKelvinToFahrenheit(temp)+" °F"
           else ->  String.format("%.2f", temp)+" K"
       }
   }

}