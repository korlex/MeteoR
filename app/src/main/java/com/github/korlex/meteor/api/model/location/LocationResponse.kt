package com.github.korlex.meteor.api.model.location

data class LocationResponse(
    val cod: String,
    val count: Int,
    val list: List<Location>,
    val message: String
)