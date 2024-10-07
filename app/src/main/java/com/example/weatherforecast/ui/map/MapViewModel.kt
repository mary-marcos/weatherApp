package com.example.weatherforecast.ui.map

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.model.StateManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.getAddress
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MapViewModel(var repository:Repos):ViewModel() {

    private val _favLocation= MutableStateFlow<StateManager<Long>>(StateManager.Loading)
    val favLocation: StateFlow<StateManager<Long>>
        get() =_favLocation




    fun saveLocationToFav(latLng: LatLng,context :Context) {
     var  adress= getAddress(latLng.latitude,latLng.longitude,context)

       var favLoc=FavItem(lat=latLng.latitude.toString(), lang =latLng.longitude.toString(),addressName = adress)
        _favLocation.value = StateManager.Loading
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _favLocation.value= StateManager.Success(repository.insertFav( favLoc ))
            } catch (ex: Exception) {
                _favLocation.value = StateManager.Error(ex.localizedMessage ?: "Unknown Error")
            }
        }
    }
}