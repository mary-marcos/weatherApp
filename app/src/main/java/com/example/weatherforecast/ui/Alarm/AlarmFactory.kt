package com.example.weatherforecast.ui.Alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.data.Repo.Repos


class AlarmFactory (var repo: Repos) :
    ViewModelProvider.Factory{


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            return AlarmViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}