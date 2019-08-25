package com.github.korlex.meteor.screen.weather.model

data class TimeItem(
    val hour: Int,
    val state: WeatherState,
    val temp: Int,
    val airPressure: Int,
    val windSpeed: Int,
    val humidity: Int
)