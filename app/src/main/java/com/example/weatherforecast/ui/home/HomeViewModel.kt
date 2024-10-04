package com.example.weatherforecast.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope


import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.ForecastWeatherData
import com.example.weatherforecast.model.ForecastWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel( var reposiory:Repos): ViewModel() {
   // var myRepo:Repos=Repos(WeatherRemoteSourceImp.getInstance())

   private var _currentWeather=MutableLiveData<CurrenWeather>()
    var currentWeather : LiveData<CurrenWeather> = _currentWeather

    private var _forecastWeather=MutableLiveData<ForecastWeatherResponse>()
    var forecastWeather : LiveData<ForecastWeatherResponse> = _forecastWeather


    private var _hourlyForecastWeather=MutableLiveData<List<ForecastWeatherData>>()
    var hourlyForecastWeather : LiveData<List<ForecastWeatherData>> = _hourlyForecastWeather

    private var _dailyForecastWeather=MutableLiveData<List<DailyWeather>>()
    var dailyForecastWeather : LiveData<List<DailyWeather>> = _dailyForecastWeather




    fun getCurrentWeatherData(latitude: Double, longitude: Double){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var result= reposiory.getCurrentWeather(latitude,longitude,"ar")

                    val weatherData =result
                    withContext(Dispatchers.Main) {
                        _currentWeather.postValue(weatherData!!)
                    }

            }catch (e:Exception){}


        }

    }


 fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                var result= reposiory.getForecastWeather(lat,lon,lang)
               var updatedlist=formatedWeather(result)

                withContext(Dispatchers.Main) {
                    getHourlyWeather(updatedlist)
                    getDailyWeather(updatedlist)
                    _forecastWeather.postValue(result)

                }
            }catch (e:Exception){}


        }
    }



    ////////

    private fun formatedWeather(response: ForecastWeatherResponse): List<ForecastWeatherData> {
        val updatedList = response.list.map { forecastData ->
            // Get the first weather icon
            val weatherIconId = forecastData.weather.firstOrNull()?.icon
            // Construct the URL for the icon
            val iconUrl = "https://openweathermap.org/img/wn/${weatherIconId}@2x.png"

            // Format the date string
            val formattedDate = formatDate(forecastData.dt_txt)

            // Return a new ForecastWeatherData with formatted date and updated icon URL
            forecastData.copy(
                dt_txt = formattedDate, // Update the dt_txt with the formatted date
                weather = forecastData.weather.map {
                    it.copy(icon = iconUrl) // Update the weather icon URL
                }
            )
        }
        return updatedList
    }

    // Function to format the date
    private fun formatDate(dtTxt: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, MM/dd/yyyy", Locale.getDefault()) // Include day name
        val date = inputFormat.parse(dtTxt) // Parse the input date string
        return outputFormat.format(date) // Format the date to the desired output format
    }
    private fun getHourlyWeather(list: List<ForecastWeatherData>): List<ForecastWeatherData> {
        // Get the date of the first item in the list
        val firstItemDate = list.firstOrNull()?.dt_txt?.substring(0, 10) ?: return emptyList()

        // Filter all items where the date matches the date of the first item
        val result = list.filter { forecast ->
            forecast.dt_txt.substring(0, 10) == firstItemDate
        }

        // Post the result to LiveData or any observer you are using
        _hourlyForecastWeather.postValue(result)

        return result
    }

    fun getDailyWeather(weatherList: List<ForecastWeatherData>): List<DailyWeather> {
        // Group by date (the date is already formatted as "EEE, MM/dd/yyyy")
        val groupedByDay = weatherList.groupBy { it.dt_txt } // Extract the date part

        // Prepare the list for daily weather
        val dailyWeatherList = mutableListOf<DailyWeather>()

        // Process each group
        groupedByDay.forEach { (date, weatherItems) ->
            // Get the day name from the formatted date
            val day = date.split(", ")[0] // Extract the day name from the formatted date

            // Find min and max temperatures for the day
            val minTemp = weatherItems.minOf { it.main.temp_min }
            val maxTemp = weatherItems.maxOf { it.main.temp_max }

            // Get the icon URL for the weather icon from the first item
            val weatherIconId = weatherItems.first().weather.first().icon
       //     val iconImg = "https://openweathermap.org/img/wn/${weatherIconId}@2x.png" // Construct the URL for the icon

            // Add a DailyWeather object to the list
            dailyWeatherList.add(DailyWeather(date, day, minTemp, maxTemp, weatherIconId))
        }
        Log.d("TAG", "getDailyWeather: ${dailyWeatherList}")
        _dailyForecastWeather.postValue(dailyWeatherList)
        return dailyWeatherList
    }

}