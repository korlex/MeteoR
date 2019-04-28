package com.github.korlex.meteor.api.model.weather

data class WeatherResponse(
    val cnt: Int,
    val cod: String,
    val list: List<Forecast>,
    val message: Double
)