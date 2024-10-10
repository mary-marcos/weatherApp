package com.example.weatherforecast.data.Repo

import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.WeatherAlarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class ReposTest{


    val fav1=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )
    var fav2=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )
    var fav3=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )
    var fav4=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )

    val alarm= WeatherAlarm(id = 1, hour = 22, minute = 11, type = "not", isActive = false)
    val alarm2= WeatherAlarm(id = 2, hour = 22, minute = 11, type = "not", isActive = false)
    val alarm3= WeatherAlarm(id = 3, hour = 22, minute = 11, type = "not", isActive = false)

    lateinit var repo:IRepos
    @Before
    fun setup(){

        var fakeRemote:FakeRemote=FakeRemote(MutableStateFlow(listOf(fav1, fav2, fav3)),)
        var fakeLocal:FakeLocal=FakeLocal(MutableStateFlow(listOf(fav1, fav2, fav4)),MutableStateFlow(listOf(alarm, alarm2, alarm3)))
        repo=Repos.getInstance(fakeRemote,fakeLocal)

    }

    @Test
    fun getfavfromRepo_getLocalDatasuccessfully() = runTest{


        val res = repo.getFavorateLocations().first()


        assertThat(res, IsEqual(listOf(fav1, fav2, fav4)))
    }


    @Test
    fun getalarmfromRepo_getLocalDatasuccessfully() = runTest{


        val res = repo.getStoredAlarms().first()


        assertThat(res, IsEqual(listOf(alarm, alarm2, alarm3)))
    }


}