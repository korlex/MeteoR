package com.github.korlex.meteor.screen.settings.location

import com.github.korlex.meteor.BasePresenter
import com.github.korlex.meteor.repo.location.LocationDataSource
import com.github.korlex.meteor.screen.settings.location.model.LocItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class LocationPresenter(
    private val view: LocationView,
    private val dataSource: LocationDataSource) : BasePresenter() {

  fun getResultByQuery(query: String) {
    view.showProgress()
    dataSource
        .getLocations(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccess, this::onError)
        .addTo(disposables)
  }

  fun saveLocation(loc: String, lat: String, lon: String) {
    dataSource.saveLocation(loc, lat, lon)
  }

  private fun onSuccess(locItems: List<LocItem>) {
    if (!locItems.isEmpty()) view.showLocations(locItems) else view.showEmpty()
  }

  private fun onError(throwable: Throwable) {
    view.showError()
  }
}