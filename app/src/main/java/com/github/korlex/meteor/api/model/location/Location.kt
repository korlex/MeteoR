package com.github.korlex.meteor.api.model.location

import com.github.korlex.meteor.api.model.Coord
import com.github.korlex.meteor.api.model.Sys

data class Location(
    val id: Int,
    val name: String,
    val dt: Int,
    val coord: Coord,
    val sys: Sys
)