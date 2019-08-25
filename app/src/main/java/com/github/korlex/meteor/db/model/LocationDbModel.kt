package com.github.korlex.meteor.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)