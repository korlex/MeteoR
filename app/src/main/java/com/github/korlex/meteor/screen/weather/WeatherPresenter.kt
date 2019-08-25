package com.github.korlex.meteor.screen.weather

import com.github.korlex.meteor.BasePresenter
import com.github.korlex.meteor.exceptions.ForceLoadNetThrowable
import com.github.korlex.meteor.exceptions.ForceLoadTimeThrowable
import com.github.korlex.meteor.repo.weather.WeatherDataSource
import com.github.korlex.meteor.screen.weather.model.WeatherSchedule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class WeatherPresenter(
    private val view: WeatherView,
    private val dataSource: WeatherDataSource) : BasePresenter() {



  fun getPlaceName(): String = dataSource.getLocation()

  fun getWeather(locale: String, forceLoad: Boolean = false) {
    if(!forceLoad) view.showProgress()
    dataSource.getWeather(locale, forceLoad)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccess, this::onError)
        .addTo(disposables)
  }

  private fun onSuccess(weatherSchedule: WeatherSchedule) {
    view.showWeather(weatherSchedule)
  }

  private fun onError(throwable: Throwable) {
    when(throwable) {
      is ForceLoadTimeThrowable -> view.showErrorTimeForceLoad(throwable)
      is ForceLoadNetThrowable  -> view.showErrorNetForceLoad()
      else                      -> view.showError()
    }
  }
}