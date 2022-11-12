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

@Database(entities = [AsteroidEntity::class], version = 1)

abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

}
@Dao
interface AsteroidDao {

    // get all asteroid entities from database and we will map it using asDomain() method
    @Query("select * from AsteroidEntity order by closeApproachDate DESC")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    // insert all asteroid we got from api response after map it using asDatabaseModel() method
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)
}

lateinit private var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase{
    synchronized(AsteroidsDatabase::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE = databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}
