package com.github.korlex.meteor.screen.weather.model

import org.threeten.bp.LocalTime

data class TimeItem(
    val hour: Int,
    val state: WeatherState,
    val temp: Int,
    val airPressure: Int,
    val windSpeed: Int,
    val humidity: Int
)