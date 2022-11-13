package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository

class RefreshDataWorker (appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params){
    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }
    var database = getDatabase(applicationContext)
    var asteroidRepository = AsteroidRepository(database)
    override suspend fun doWork(): Result {
        return try {
            asteroidRepository.refreshAsteroid()
            Result.success()
        }catch (e : Exception) {
            Result.retry()
        }
    }
}

