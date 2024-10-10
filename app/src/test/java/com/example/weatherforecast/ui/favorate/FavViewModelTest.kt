package com.example.weatherforecast.ui.favorate

import com.example.weatherforecast.data.Repo.IRepos
import com.example.weatherforecast.model.FavItem
import com.example.weatherforecast.model.StateManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Duration


class FavViewModelTest{


    lateinit var repo:FakeRepo
    lateinit var  favViewModel:FavViewModel
    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setup() {
        testDispatcher = TestCoroutineDispatcher()
        repo=FakeRepo()
        favViewModel=FavViewModel(repo)
    }




    @Test
    fun deleteFavLocation_insertLocationToFav_deleteFavLocation_locationIsDeletedSuccessfully() = runTest {

            val fav1=FavItem(addressName = "add1", lang = "12.2" , lat ="11.1", id = 1 )
            favViewModel.deleteLocationfromFav(fav1)
            assertTrue(favViewModel.deletefavLocation.value is StateManager.Loading)
            testDispatcher.scheduler.advanceUntilIdle()
            assertTrue(favViewModel.deletefavLocation.value is StateManager.Success)

        }






//    @Test
//    fun getfavlist_returnFavlist()= runTest {
//        // val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
//
//
//        favViewModel.getLocations()
//        assertTrue(favViewModel.favLocation.value is StateManager.Loading)
//        testDispatcher.scheduler.advanceUntilIdle()
//        assertTrue(favViewModel.favLocation.value is StateManager.Success)
//
//    }


}