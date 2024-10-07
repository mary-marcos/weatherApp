package com.example.weatherforecast.ui.favorate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.Repo.Repos
import com.example.weatherforecast.ui.map.MapViewModel

class FavLocationFctory (var repo: Repos) :
    ViewModelProvider.Factory{


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            return FavViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}