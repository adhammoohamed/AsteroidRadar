package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Dao
interface AsteroidDao {

    // get all asteroid entities from database and we will map it using asDomain() method
    @Query("select * from AsteroidEntity order by closeApproachDate desc")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    //get all asteroids in one day
    @Query("select * from asteroidentity where closeApproachDate is :date order by closeApproachDate desc")
    fun getAsteroidsToday(date: String): LiveData<List<AsteroidEntity>>

    // get all asteroids in week
    @Query ("select * from AsteroidEntity where closeApproachDate between :startDate and :endDate order by closeApproachDate desc")
    fun getAsteroidsInWeek(startDate: String , endDate: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE
}