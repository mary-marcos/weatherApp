package com.example.weatherforecast.ui.Alarm

import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.WeatherAlarm

interface OnAlarmClicked {
    fun respondalarm(favItem: WeatherAlarm)
    fun deletitemalarm(favItem: WeatherAlarm)
}
