package com.example.weatherforecast.data.localData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.model.FavItem

@Database(entities = [FavItem::class], version = 2, exportSchema = false)
abstract class  FavDataBase : RoomDatabase(){


    abstract fun favDao(): FavDao

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

