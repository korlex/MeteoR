package com.github.korlex.meteor.db.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ForecastDbModel(
    val dtTxt: String,
    val dt: Long,
    val mainHumidity: Int,
    val mainPressure: Double,
    val mainTemp: Double,
    val weatherDesc: String,
    val weatherIcon: String,
    val weatherId: Int,
    val weatherMain: String,
    val windSpeed: Double,

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
)