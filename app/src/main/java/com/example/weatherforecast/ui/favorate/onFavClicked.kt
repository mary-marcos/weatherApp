package com.example.weatherforecast.ui.favorate

import com.example.weatherforecast.model.FavItem

interface onFavClicked {
    fun respond(favItem: FavItem)
    fun deletitem(favItem: FavItem)
}