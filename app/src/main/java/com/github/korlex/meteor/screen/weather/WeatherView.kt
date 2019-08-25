package com.github.korlex.meteor.screen.weather

import com.github.korlex.meteor.exceptions.ForceLoadTimeThrowable
import com.github.korlex.meteor.screen.weather.model.WeatherSchedule

interface WeatherView {
  fun showWeather(weatherSchedule: WeatherSchedule)
  fun showProgress()
  fun showError()
  fun showErrorTimeForceLoad(throwable: ForceLoadTimeThrowable)
  fun showErrorNetForceLoad()
}