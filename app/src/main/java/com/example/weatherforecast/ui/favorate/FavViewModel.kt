package com.example.weatherforecast.ui.favorate

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.Repo.IRepos
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.model.getAddress
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel (var repository: IRepos): ViewModel() {

    private val _favLocation= MutableStateFlow<StateManager<List<FavItem>>>(StateManager.Loading)
    val favLocation: StateFlow<StateManager<List<FavItem>>>
        get() =_favLocation

    private val _deletefavLocation= MutableStateFlow<StateManager<Int>>(StateManager.Loading)
    val deletefavLocation: StateFlow<StateManager<Int>>
        get() =_deletefavLocation



    fun getLocations(){
       // Log.d("TAG", "getLocations:    getlovaaaatioooon ")
        _favLocation.value=StateManager.Loading
        viewModelScope.launch(Dispatchers.IO) {

          var list=  repository.getFavorateLocations()

            list.catch {
                _favLocation.value = StateManager.Error("error Occured")
            }.collect{
                    data ->

                _favLocation.value = StateManager.Success(data)
            }
        }


    }



    fun deleteLocationfromFav(favitem:FavItem) {



        _deletefavLocation.value = StateManager.Loading
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _deletefavLocation.value= StateManager.Success(repository.deleteFav(favitem))
            } catch (ex: Exception) {
                _deletefavLocation.value = StateManager.Error(ex.localizedMessage ?: "Unknown Error")
            }
        }
    }


}