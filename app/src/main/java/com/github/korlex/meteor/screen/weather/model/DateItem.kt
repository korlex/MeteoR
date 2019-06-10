package com.github.korlex.meteor.screen.weather.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class DateItem(
    val date: String,
    val startHour: Int,
    val endHour: Int,
    val avgState: WeatherState,
    val avgTemp: Int
)