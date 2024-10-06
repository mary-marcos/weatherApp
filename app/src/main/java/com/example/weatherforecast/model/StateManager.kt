package com.example.weatherforecast.model


sealed class StateManager<out T> {
    data class Success<out T>(val data: T) : StateManager<T>()
    data class Error(val message: String?) : StateManager<Nothing>()
    object Loading : StateManager<Nothing>()

}