package com.github.korlex.meteor.repo.weather

import com.github.korlex.meteor.screen.weather.model.WeatherSchedule
import io.reactivex.Single

interface WeatherDataSource {
  fun getWeather(locale: String, forceLoad: Boolean): Single<WeatherSchedule>
  fun getLocation(): String
}