package com.github.korlex.meteor.repo.weather

import com.github.korlex.meteor.screen.weather.model.WeatherSchedule
import io.reactivex.Single

interface WeatherDataSource {
  fun getWeather2(locale: String): Single<WeatherSchedule>
  fun getLocation(): String
}