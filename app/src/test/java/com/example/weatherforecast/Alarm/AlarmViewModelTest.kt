package com.example.weatherforecast.Alarm

import com.example.weatherforecast.data.localData.AlarmDao
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.StateManager
import com.example.weatherforecast.model.WeatherAlarm
import com.example.weatherforecast.ui.Alarm.AlarmViewModel
import com.example.weatherforecast.ui.favorate.FakeRepo
import com.example.weatherforecast.ui.favorate.FavViewModel
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class AlarmViewModelTest{

    lateinit var repo: FakeRepo
    lateinit var  alarmviewModel: AlarmViewModel
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        testDispatcher = TestCoroutineDispatcher()
        repo= FakeRepo()
        alarmviewModel= AlarmViewModel(repo)
    }
    @Test
    fun deleteAlarm_deleteonealarm_locationIsDeletedSuccessfully() = runTest {

        val alarm= WeatherAlarm(id = 1, hour = 22, minute = 11, type = "not", isActive = false)

        alarmviewModel.deleteAlarm(alarm)
        assertTrue(alarmviewModel.deleteAlarm.value is StateManager.Loading)
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(alarmviewModel.deleteAlarm.value is StateManager.Success)

    }

}