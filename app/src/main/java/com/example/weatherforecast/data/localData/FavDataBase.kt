package com.example.weatherforecast.data.localData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.model.CurrentWeatherDataEntity
import com.example.weatherforecast.model.DailyWeather
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.HourlyWeather
import com.example.weatherforecast.model.WeatherAlarm

@Database(entities = [FavItem::class,WeatherAlarm::class,CurrentWeatherDataEntity::class,HourlyWeather::class,DailyWeather::class], version = 6, exportSchema = false)
abstract class  FavDataBase : RoomDatabase(){

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun favDao(): FavDao
    abstract fun AlarmDao(): AlarmDao

    companion object {
        // Singleton instance of the database
        @Volatile
        private var INSTANCE: FavDataBase? = null


        fun getInstance(context: Context): FavDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDataBase::class.java,
                    "Weather_database"
                )

                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

