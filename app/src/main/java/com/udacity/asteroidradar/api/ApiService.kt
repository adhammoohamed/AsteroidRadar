package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Moshi library to parse the JSON to kotlin objects
var moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// okHttp client to load resources faster
var okHttp = OkHttpClient.Builder()
    .connectTimeout(1, TimeUnit.MINUTES)
    .readTimeout(1, TimeUnit.MINUTES)
    .writeTimeout(1, TimeUnit.MINUTES)
    .build()

// retroFit to connect with the api
var retrofitAsteroids = Retrofit.Builder()
    .client(okHttp)
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()


// this api to connect and fetch data from the server
interface AsteroidApiService {
    @GET("neo/rest/v1/feed?")
    suspend fun getAsteroids(
        @Query("start_date") starDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): String

}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofitAsteroids.create(AsteroidApiService::class.java)
    }

}

// get picture of day
val retrofitPic = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

// this api to connect and fetch data from the server
interface PicOfDayApiService {
    @GET("planetary/apod?")
    suspend fun getPicOfDay(@Query("api_key") apiKey: String): PictureOfDay
}

val picOfDayApiService: PicOfDayApiService by lazy {
    retrofitPic.create(PicOfDayApiService::class.java)
}