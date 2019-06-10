package com.github.korlex.meteor.api.model.weather

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("dt_txt")
    val dtTxt: String,
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)