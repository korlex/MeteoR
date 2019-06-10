package com.github.korlex.meteor.screen.weather

import com.github.korlex.meteor.screen.weather.model.WeatherSchedule

interface WeatherView {
  fun showWeather2(weatherSchedule: WeatherSchedule)
  fun showProgress2()
  fun showError2()
}