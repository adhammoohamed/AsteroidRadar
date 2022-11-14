package com.udacity.asteroidradar

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Constants {
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    val API_KEY = BuildConfig.API_KEY
    const val DEMO_API_KEY = "DEMO"

    // get the current date to add in start_date parameter
    var startDate = LocalDateTime.now()

    // get the end date
    var endDate = startDate.plusDays(Constants.DEFAULT_END_DATE_DAYS.toLong())

    // formatter to format dates to map the parameter using the provided method
    var formatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)

    var formattedStartDate = startDate.format(formatter)
    var formattedEndDate = endDate.format(formatter)
}