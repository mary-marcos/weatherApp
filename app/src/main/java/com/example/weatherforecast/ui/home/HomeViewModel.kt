package com.example.weatherforecast.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope


import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.Repo.WeatherRemoteSourceImp
import com.example.weatherforecast.data.SharePrefrenceData
import com.example.weatherforecast.model.CurrenWeather
import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.ForecastWeatherData
import com.example.weatherforecast.model.ForecastWeatherResponse
import com.example.weatherforecast.model.HourlyWeather
import com.example.weatherforecast.model.StateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel( var reposiory:Repos): ViewModel() {
   // var myRepo:Repos=Repos(WeatherRemoteSourceImp.getInstance())

   private var _currentWeather= MutableStateFlow<StateManager<CurrentWeatherDataEntity>>(StateManager.Loading)
    var currentWeather : StateFlow<StateManager<CurrentWeatherDataEntity>> = _currentWeather

    private var _forecastWeather=MutableLiveData<ForecastWeatherResponse>()
    var forecastWeather : LiveData<ForecastWeatherResponse> = _forecastWeather


    private var _hourlyForecastWeather = MutableStateFlow<StateManager<List<HourlyWeather>>>(StateManager.Loading)
    var hourlyForecastWeather :       StateFlow<StateManager<List<HourlyWeather>>>   = _hourlyForecastWeather

    private var _dailyForecastWeather= MutableStateFlow<StateManager<List<DailyWeather>>>(StateManager.Loading)
    var dailyForecastWeather :  StateFlow<StateManager<List<DailyWeather>>>  = _dailyForecastWeather


    fun getCurrentWeatherData(latitude: Double, longitude: Double,context: Context) {
        var sharePrefrenceData=SharePrefrenceData(context)
        viewModelScope.launch(Dispatchers.IO) {


            reposiory.getCurrentWeather(latitude, longitude, sharePrefrenceData.getSavedLanguage()?:"en").map { data ->
                val currentEntity = CurrentWeatherDataEntity(
                    city = data.name,
                    temp = sharePrefrenceData.getFormatedUnit(data.main.temp),
                    windSpeed = data.wind.speed.toString(),
                    clouds = data.clouds.all.toString(),
                    humidity = data.main.humidity.toString(),
                    id = data.id,
                    description = data.weather.get(0).description,
                    iconCode = "https://openweathermap.org/img/wn/${data.weather.get(0).icon}@2x.png",
                    feelsLike = data.main.feels_like.toString(),
                    pressure = data.main.pressure.toString()

                )
                currentEntity
            }
                .catch {
                    _currentWeather.value = StateManager.Error("errorr message")
                }
                .collect { data ->

                    _currentWeather.value = StateManager.Success(data)
                }
        }
    }


    fun getForecastWeather(
        lat: Double,
        lon: Double,
        context: Context
    ) {
        val sharePrefrenceData = SharePrefrenceData(context)


        viewModelScope.launch(Dispatchers.IO) {

            reposiory.getForecastWeather(lat, lon, sharePrefrenceData.getSavedLanguage() ?: "en")
                .map { response ->

                    response.list.map { forecastData ->

                        val weatherIconId = forecastData.weather.firstOrNull()?.icon


                        val iconUrl = "https://openweathermap.org/img/wn/${weatherIconId}@2x.png"


                      val formattedDate = formatDate(forecastData.dt_txt)


                        forecastData.copy(
                           dt_txt = formattedDate,
                            weather = forecastData.weather.map {
                                it.copy(icon = iconUrl)
                            }
                        )
                    }
                }.catch {
                    _hourlyForecastWeather.value = StateManager.Error("errorr message")
                 _dailyForecastWeather.value = StateManager.Error("errorr message")
                }

                .collect { updatedList: List<ForecastWeatherData> ->

                    getHourlyWeather(updatedList, sharePrefrenceData)
                    getDailyWeather(updatedList, sharePrefrenceData)
                }
        }
    }









    private fun formatDate(dtTxt: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm, EEE, MM/dd/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dtTxt)
        return outputFormat.format(date)
    }

    private fun getHourlyWeather(list: List<ForecastWeatherData>,sharedpref: SharePrefrenceData): List<HourlyWeather> {
        val hourlyWeatherList = mutableListOf<HourlyWeather>()


        val firstItemDate = list.firstOrNull()?.dt_txt?.substring(5, 10) ?: return emptyList()


        val filteredForecasts = list.filter { forecast ->
            forecast.dt_txt.substring(5, 10) == firstItemDate
        }

        val result = filteredForecasts.map { forecast ->

            val hour = forecast.dt_txt.substring(0, 4)
            val temp = sharedpref.getFormatedUnit( forecast.main.temp )
            val iconUrl = forecast.weather.firstOrNull()?.icon ?: ""


            HourlyWeather(
                hour = hour,
                Temp = temp,
                iconImg = iconUrl
            )
        }


        _hourlyForecastWeather.value = StateManager.Success(result)


        return result
    }


    fun getDailyWeather(weatherList: List<ForecastWeatherData>,sharedpref:SharePrefrenceData): List<DailyWeather> {

        val groupedByDay = weatherList.groupBy { it.dt_txt.substring(5) }
        val dailyWeatherList = mutableListOf<DailyWeather>()
        groupedByDay.forEach { (date, weatherItems) ->
            val day = date.split(", ")[1]

            val minTemp =  sharedpref.getFormatedUnit(weatherItems.minOf { it.main.temp_min })
            val maxTemp = sharedpref.getFormatedUnit(weatherItems.maxOf { it.main.temp_max })

            val weatherIconId = weatherItems.first().weather.first().icon

            dailyWeatherList.add(DailyWeather(date, day,"$minTemp/$maxTemp", weatherIconId))
        }
        Log.d("TAG", "getDailyWeather: ${dailyWeatherList}")
        _dailyForecastWeather.value = StateManager.Success(dailyWeatherList)

        return dailyWeatherList
    }

}