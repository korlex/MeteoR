package com.github.korlex.meteor.screen.weather

import com.github.korlex.meteor.BasePresenter
import com.github.korlex.meteor.repo.weather.WeatherDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class WeatherPresenter(
    private val view: WeatherView,
    private val dataSource: WeatherDataSource) : BasePresenter() {



  fun getPlaceName(): String = dataSource.getLocation()

  fun getWeather() {
    view.showProgress()
    dataSource
        .getWeather()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccess, this::onError)
        .addTo(disposables)
  }

  private fun onSuccess(items: List<Any>) {
    if(!items.isEmpty()) view.showWeather(items) else view.showEmpty()
  }

  private fun onError(throwable: Throwable) {
    view.showError()
  }
}