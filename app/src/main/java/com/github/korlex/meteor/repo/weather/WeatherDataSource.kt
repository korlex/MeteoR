package com.github.korlex.meteor.repo.weather

import io.reactivex.Single

interface WeatherDataSource {
  fun getWeather(): Single<List<Any>>
  fun getLocation(): String
}