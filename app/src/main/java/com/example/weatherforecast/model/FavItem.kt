package com.example.weatherforecast.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

@Entity(tableName = "fav_table")
data class FavItem(@PrimaryKey(autoGenerate = true)   val id: Int = 0,
              var lat:String,var lang:String
              ,
    var addressName:String): Serializable