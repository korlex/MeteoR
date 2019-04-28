package com.github.korlex.meteor.screen.weather.model

import org.threeten.bp.LocalTime

data class ForecastItem(
    val time: LocalTime,
    val condition: String,
    val iconId: Int,
    val maxTemp: Double,
    val minTemp: Double
)