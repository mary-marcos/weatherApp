package com.example.weatherforecast.data.Repo

import android.content.Context
import com.example.weatherforecast.data.localData.FavDataBase
import com.example.weatherforecast.data.localData.FavDao
import com.example.weatherforecast.model.FavItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalSource (private val myfavDao: FavDao){

    companion object {
        @Volatile
        private var INSTANCE: LocalSource? = null

        fun getInstance(context: Context): LocalSource {
            return INSTANCE ?: synchronized(this) {
                val dao = FavDataBase.getInstance(context).favDao()
                val instance = LocalSource(dao)
                INSTANCE = instance
                instance
            }
        }
    }


    suspend fun insertFav(fav: FavItem):Long {
        var result= myfavDao.setOneFav(fav)
        return result
    }

    suspend fun deleteFav(fav: FavItem):Int {
     return   myfavDao.deleteOneFav(fav)
    }

    suspend fun getStoredLocation(): Flow<List<FavItem>> {
//        return flow {val result= apiService.getCurrentWeather(lat, lon, apiKey, lang)
//            emit(result)}

        return myfavDao.getAllFav()
    }
}