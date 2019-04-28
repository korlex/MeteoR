package com.github.korlex.meteor.screen.weather

interface WeatherView {
  fun showWeather(items: List<Any>)
  fun showProgress()
  fun showEmpty()
  fun showError()
}