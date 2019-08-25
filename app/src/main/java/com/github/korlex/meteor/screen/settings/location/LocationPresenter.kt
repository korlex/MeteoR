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

  fun getResultByCoord(lat: Double, lon: Double) {
    view.showProgress()
    dataSource
        .getLocationsByCoord(lat, lon)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccessRemote, this::onError)
        .addTo(disposables)
  }

  fun getLocationsRemote(query: String? = null) {
    view.showProgress()
    dataSource
        .getLocationsByQuery(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccessRemote, this::onError)
        .addTo(disposables)
  }

  fun getLocationsDb() {
    view.showProgress()
    dataSource
        .getLocationsFromDb()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccessDb, this::onError)
        .addTo(disposables)
  }


  fun saveLocations(locations: List<LocItem>, pickedPosition: Int) {
    with(dataSource) {
      val pickedItem = locations[pickedPosition]
      savePickedLocation(pickedItem.placeId, pickedItem.placeName, pickedPosition)
      saveSearchedLocations(locations)
    }
  }

  private fun onSuccessRemote(locItems: List<LocItem>) {
    if (locItems.isNotEmpty()) view.showLocationsRemote(locItems) else view.showEmpty()
  }

  private fun onSuccessDb(locItems: List<LocItem>) {
    if (locItems.isNotEmpty()) view.showLocationsDb(locItems) else view.showEmpty()
  }

  private fun onError(throwable: Throwable) {
    view.showError()
  }
}