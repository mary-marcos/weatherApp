package com.example.weatherforecast.data.localData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.model.FavItem
import kotlinx.coroutines.flow.Flow
@Dao
interface FavDao {

    @Query("SELECT * FROM fav_table")
    fun getAllFav(): Flow<List<FavItem>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setAllFav(vararg fav: FavItem): List<Long>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setOneFav(fav: FavItem): Long


    @Delete
    suspend fun deleteOneFav(fav: FavItem): Int
}