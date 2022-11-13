package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDataBaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(var database: AsteroidsDatabase) {

    var asteroids = Transformations.map(database.asteroidDao.getAllAsteroids()) {
        it.asDomainModel()
    }

    // get asteroids in day
    var asteroidsInToday =
        Transformations.map(database.asteroidDao.getAsteroidsToday(Constants.formattedStartDate)) {
            it.asDomainModel()
        }

    // get asteroids in week
    var asteroidsInWeek = Transformations.map(
        database.asteroidDao.getAsteroidsInWeek(
            Constants.formattedStartDate,
            Constants.formattedEndDate
        )
    ) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroid() {
        withContext(Dispatchers.IO) {
            try {

                val apiResponse = AsteroidApi.retrofitService.getAsteroids(
                    Constants.formattedStartDate,
                    Constants.formattedEndDate,
                    Constants.API_KEY
                )

                val jsonObject = JSONObject(apiResponse)
                val responseArrayList: ArrayList<Asteroid> = parseAsteroidsJsonResult(jsonObject)
                val mappedResult = responseArrayList.asDataBaseModel()
                database.asteroidDao.insertAll(*mappedResult)
                Log.i("Adham", "in refresh ${database.asteroidDao.getAllAsteroids().value}")
                Log.i("Adham", "Success")
            } catch (e: Exception) {
                Log.i("Adham", "${asteroids.value}")
                Log.i("Adham", "Failed")
            }
        }
    }
}