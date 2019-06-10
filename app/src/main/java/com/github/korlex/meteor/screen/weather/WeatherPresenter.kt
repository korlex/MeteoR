package com.github.korlex.meteor.screen.weather

import com.github.korlex.meteor.BasePresenter
import com.github.korlex.meteor.repo.weather.WeatherDataSource
import com.github.korlex.meteor.screen.weather.model.WeatherSchedule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class WeatherPresenter(
    private val view: WeatherView,
    private val dataSource: WeatherDataSource) : BasePresenter() {



  fun getPlaceName(): String = dataSource.getLocation()

  fun getWeather2(locale: String) {
    view.showProgress2()
    dataSource
        .getWeather2(locale)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccess2, this::onError2)
        .addTo(disposables)
  }

  private fun onSuccess2(weatherSchedule: WeatherSchedule) {
    view.showWeather2(weatherSchedule)
  }

  private fun onError2(throwable: Throwable) {
    view.showError2()
  }
}