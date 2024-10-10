package com.example.weatherforecast.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "alarm_table")
class WeatherAlarm (
                 @PrimaryKey(autoGenerate = false)
                    val duration: Long,
                    var hour:Int,
                    var type: String,
                    var minute:Int,
                 var isActive:Boolean
                   ): Serializable

