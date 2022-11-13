package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.picOfDayApiService
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class MenuFilter { TODAY, SAVED, WEEK , ALL}

enum class ResponseStatues { SUCCESS, LOADING }

class MainViewModel(application: Application) : ViewModel() {

    private var database = getDatabase(application)
    private var asteroidRepository = AsteroidRepository(database)

    var filter: MutableLiveData<MenuFilter> = MutableLiveData<MenuFilter>(MenuFilter.ALL)

    // using transformation.switchMap() to switch the value when the user clicked on the filter menu
    var asteroids = Transformations.switchMap(filter){
        return@switchMap when(it){
            MenuFilter.WEEK -> asteroidRepository.asteroidsInWeek
            MenuFilter.TODAY -> asteroidRepository.asteroidsInToday
            MenuFilter.SAVED -> asteroidRepository.asteroids
            else -> asteroidRepository.asteroids
        }
    }

    // picture of day response
    private var _picOfDayResponse = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _picOfDayResponse

    // response status
    private var _responseStatus = MutableLiveData<ResponseStatues>()
    val responseStatus: LiveData<ResponseStatues>
        get() = _responseStatus


    init {
        viewModelScope.launch {
            _responseStatus.value = ResponseStatues.LOADING
            asteroidRepository.refreshAsteroid()

            // response succeed
            _responseStatus.value = ResponseStatues.SUCCESS
            Log.i("Adham", "Asteroids in vm ${asteroids.value}")

            getPicOfDay()
        }
    }

    private suspend fun getPicOfDay() {
        viewModelScope.launch {
            try {
                var response = picOfDayApiService.getPicOfDay(Constants.API_KEY)
                _picOfDayResponse.value = response
            } catch (e: Exception) {
                Log.i("Adham", "pic Failed")
            }
        }
    }
}

// make factory class to add parameter in viewModel
class Factory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unable to construct viewModel")
    }
}