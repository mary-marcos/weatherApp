package com.example.weatherforecast.ui.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.Repo.IRepos
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.data.SharePrefrenceData
import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmViewModel (var repository: IRepos): ViewModel() {

    private val _getAlarms= MutableStateFlow<StateManager<List<WeatherAlarm>>>(StateManager.Loading)
    val getAlarms: StateFlow<StateManager<List<WeatherAlarm>>>
        get() =_getAlarms

    private val _deleteAlarm= MutableStateFlow<StateManager<Int>>(StateManager.Loading)
    val deleteAlarm: StateFlow<StateManager<Int>>
        get() =_deleteAlarm

    private val _inserAlarm= MutableStateFlow<StateManager<Long>>(StateManager.Loading)
    val inserAlarm: StateFlow<StateManager<Long>>
        get() =_inserAlarm

    private var _currentWeatherdes= MutableStateFlow<StateManager<String>>(StateManager.Loading)
    var currentWeatherdes : StateFlow<StateManager<String>> = _currentWeatherdes




    fun getAlrmss(){
        Log.d("TAG", "getLocations:    getlovaaaatioooon ")
        _getAlarms.value= StateManager.Loading
        viewModelScope.launch(Dispatchers.IO) {

            var list=  repository.getStoredAlarms()

            list.catch {
                _getAlarms.value = StateManager.Error("error Occured")
            }.collect{
                    data ->

                _getAlarms.value = StateManager.Success(data)
            }
        }


    }



    fun deleteAlarm(alarm: WeatherAlarm) {



        _deleteAlarm.value = StateManager.Loading
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _deleteAlarm.value= StateManager.Success(repository.deleteAlarm(alarm))
            } catch (ex: Exception) {
                _deleteAlarm.value = StateManager.Error(ex.localizedMessage ?: "Unknown Error")
            }
        }
    }

//    fun getDescription(latitude:Double,longitude:Double,context: Context){
//        var sharePrefrenceData= SharePrefrenceData(context)
//       viewModelScope.launch(Dispatchers.IO) {
////            if(sharePrefrenceData.getLocationMode()=="Map"){
////                val savedLatLng = sharePrefrenceData.getLatLng()
////
////            }else{}
//
//
//            repository.getCurrentWeather(latitude, longitude, sharePrefrenceData.getSavedLanguage()?:"en").map { data ->
//
//
//                data.weather.get(0).description
//            }
//                .catch {
//                    _currentWeatherdes.value = StateManager.Error("errorr message")
//                }
//                .collect { data ->
//
//                    _currentWeatherdes.value = StateManager.Success(data)
//                }
//        }
//    }

    fun addWeatherAlert(weatherAlarm: WeatherAlarm,context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        viewModelScope.launch(Dispatchers.IO)  {


            try {
                _inserAlarm.value= StateManager.Success(repository.insertOnerAlarm( weatherAlarm ))
            } catch (ex: Exception) {
                _inserAlarm.value = StateManager.Error(ex.localizedMessage ?: "Unknown Error")
            }

//
//            val alarmIntent = Intent(
//                context,
//                MyAlarmReceiver::class.java
//            )
////            alarmIntent.putExtra(
////                AlarmReceiver.ALERT_TYPE_EXTRA,
////                weatherAlarm.type
////            )
//            val pendingIntent = PendingIntent.getBroadcast(
//                context,
//                weatherAlarm.id,
//                alarmIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//
//            val calendar = Calendar.getInstance()
//            calendar.set(Calendar.HOUR_OF_DAY, weatherAlarm.hour)
//            calendar.set(Calendar.MINUTE, weatherAlarm.minute)
//            calendar.set(Calendar.SECOND, 0)
//            val alarmTime = calendar.timeInMillis
//
//            Log.d("WeatherAlertsViewModel", "Alarm time: $alarmTime")
//
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP,
//                        alarmTime,
//                        pendingIntent
//                    )
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager.setExact(
//                        AlarmManager.RTC_WAKEUP,
//                        alarmTime,
//                        pendingIntent
//                    )
//                } else {
//                    alarmManager.set(
//                        AlarmManager.RTC_WAKEUP,
//                        alarmTime,
//                        pendingIntent
//                    )
//                }
//            } catch (e: SecurityException) {
//                Log.e("WeatherAlertsViewModel", "SecurityException: ${e.message}")
//                // Handle the SecurityException here
//            }
        }
    }





}