package com.github.korlex.meteor.api.model.location

data class Location(
    val id: Int,
    val name: String,
    val dt: Int,
    val coord: Coord,
    val sys: Sys
)