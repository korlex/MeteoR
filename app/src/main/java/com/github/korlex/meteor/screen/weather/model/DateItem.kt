package com.github.korlex.meteor.screen.weather.model

data class DateItem(
    val date: String,
    val startHour: Int,
    val endHour: Int,
    val avgState: WeatherState,
    val avgTemp: Int
)