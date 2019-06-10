package com.github.korlex.meteor.screen.weather.model

data class WeatherSchedule(
    val dateItems: List<DateItem>,
    val timeItems: List<List<TimeItem>>
)