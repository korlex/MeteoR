package com.github.korlex.meteor.api.model.weather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)