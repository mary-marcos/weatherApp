package com.example.weatherforecast.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.NetworkCheck

import com.example.weatherforecast.model.formatDate
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
import kotlinx.coroutines.flow.firstOrNull
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

            if (NetworkCheck.isNetworkAvailable(context)){
            reposiory.getCurrentWeather(latitude, longitude, sharePrefrenceData.getSavedLanguage()?:"en").map { data ->
                val currentEntity = CurrentWeatherDataEntity(
                    city = data.name,
                    temp = sharePrefrenceData.getFormatedUnit(data.main.temp),
                    windSpeed = data.wind.speed.toString(),
                    clouds = data.clouds.all.toString(),
                    humidity = data.main.humidity.toString(),
                    id = data.id,
                    description = data.weather.get(0).description,
                    iconCode =data.weather.get(0).icon,
                  //  iconCode = "https://openweathermap.org/img/wn/${data.weather.get(0).icon}@2x.png",
                    feelsLike = data.main.feels_like.toString(),
                    pressure = data.main.pressure.toString()

                )
                try {
                    val existingData = reposiory.getCurrentWeatherlocal().firstOrNull()

                    if (existingData != null && existingData.isNotEmpty()) {
                        reposiory.deleteCurrentWeather()
                    }

               launch {
                   try {
                 var k=  reposiory.setCurrentWeather(currentEntity)
                   Log.d("TAG", "getCurrentWeatherData result insert : $k")
               }catch (ex: Exception){
                   Log.d("TAG", "getCurrentWeatherDataaaaaaaaaaaaaaaaaaaa: $ex")  }

               }

                }catch  (ex: Exception){
                    Log.d("TAG", "getCurrentWeatherData: $ex")}


                currentEntity
            }
                .catch {
                    _currentWeather.value = StateManager.Error("errorr message")
                }
                .collect { data ->

                    _currentWeather.value = StateManager.Success(data)
                }
            }else{
                reposiory.getCurrentWeatherlocal().catch { _currentWeather.value = StateManager.Error("No local data available, check network") }
                .collect{data ->
                    if (data.isNotEmpty()) {
                        val current = data[0]
                        _currentWeather.value = StateManager.Success(current)
                    } else {
                        _currentWeather.value = StateManager.Error("No local data available")
                    }
                }
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
            if (NetworkCheck.isNetworkAvailable(context)){
            reposiory.getForecastWeather(lat, lon, sharePrefrenceData.getSavedLanguage() ?: "en")
                .map { response ->

                    response.list.map { forecastData ->

                        val weatherIconId = forecastData.weather.firstOrNull()?.icon


                      //  val iconUrl = "https://openweathermap.org/img/wn/${weatherIconId}@2x.png"


                      val formattedDate = formatDate(forecastData.dt_txt)


                        forecastData.copy(
                           dt_txt = formattedDate,
                            weather = forecastData.weather.map {
                                it.copy(icon = weatherIconId?:"nonimg")
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
        } else{
            launch {   reposiory.getDailyWeather().catch {
                _dailyForecastWeather.value = StateManager.Error("no data yet check network") }
                .collect{data->_dailyForecastWeather.value=StateManager.Success(data)
                    Log.d("TAG", "getForecastWeathertoShow:$data ")
                } }
                launch {  reposiory.getHourlyWeather() .catch {
                    _hourlyForecastWeather.value = StateManager.Error("no data") }
                    .collect{data->_hourlyForecastWeather.value=StateManager.Success(data)} }
        }
        }

    }


    private fun getHourlyWeather(list: List<ForecastWeatherData>,sharedpref: SharePrefrenceData): List<HourlyWeather> {

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

//handle Local
        viewModelScope.launch(Dispatchers.IO) {
            val rowsDeleted = reposiory.deleteAllHourlyWeather()
            var m=    reposiory.setAllHourlyWeather(result)
                Log.d("TAG", "getHourlyWeatherrrrrrrrrrrrrrrrrrrrr: $m ")
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

            dailyWeatherList.add(
                DailyWeather(
                    date = date, day = day, minmaxTemp =
                    "$minTemp/$maxTemp", iconImg =
                    weatherIconId
                )
            )

        }

        Log.d("TAG", "getDailyWeather: ${dailyWeatherList}")
//handle Local
        viewModelScope.launch(Dispatchers.IO) {
            try {
                 reposiory.deleteAllDailyWeather()
               var resultinsertdaily= reposiory.setAllDailyWeather(dailyWeatherList)
                Log.d("TAG", "resultinsertdaily:   resultinsertdaily $resultinsertdaily  ")
            }catch (ec:Exception){
                Log.d("TAG", "insertDailyWeather:   exceptioooon $ec  ")}
        }
        _dailyForecastWeather.value = StateManager.Success(dailyWeatherList)

        return dailyWeatherList
    }

}