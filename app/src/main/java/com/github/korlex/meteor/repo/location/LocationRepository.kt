package com.github.korlex.meteor.repo.location

import com.github.korlex.meteor.api.MeteorService
import com.github.korlex.meteor.api.model.location.Location
import com.github.korlex.meteor.db.MeteorDb
import com.github.korlex.meteor.db.dao.LocationDao
import com.github.korlex.meteor.db.model.LocationDbModel
import com.github.korlex.meteor.preferences.MeteorPrefs
import com.github.korlex.meteor.screen.settings.location.model.LocItem
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class LocationRepository @Inject constructor (
    private val meteorService: MeteorService,
    private val meteorPrefs: MeteorPrefs,
    private val meteorDb: MeteorDb) : LocationDataSource {


  override fun getLocationsByCoord(lat: Double, lon: Double): Single<List<LocItem>> {
    return meteorService.getLocationsByCoord(lat, lon).map { remoteLocationsToLocItems(it.list) }
  }

  override fun savePickedLocation(placeId: Int, placeName: String, placePosition: Int) {
    meteorPrefs.locId.set(placeId)
    meteorPrefs.locName.set(placeName)
    meteorPrefs.locPos.set(placePosition)
  }

  override fun saveSearchedLocations(locations: List<LocItem>) {
    Single
        .just(meteorDb.locationDao())
        .subscribeOn(Schedulers.io())
        .subscribe(object: DisposableSingleObserver<LocationDao>() {
          override fun onSuccess(locDao: LocationDao) {
            locDao.replaceLocations(locItemsToDbLocations(locations))
            dispose()
          }

          override fun onError(t: Throwable) {
            Timber.e(t)
            dispose()
          }
        })
  }


  override fun getLocationsByQuery(q: String?): Single<List<LocItem>> {
    return meteorService
        .getLocationsByQuery(q)
        .map { remoteLocationsToLocItems(it.list) }
  }

  override fun getLocationsFromDb(): Single<List<LocItem>> {
    return meteorDb
        .locationDao()
        .getLocations()
        .map { dbLocationsToLocItems(it) }
  }


  private fun locItemsToDbLocations(locItems: List<LocItem>): List<LocationDbModel> =
      locItems.map { LocationDbModel(it.placeId, it.placeName, it.latitude, it.longitude, it.country) }

  private fun dbLocationsToLocItems(locations: List<LocationDbModel>): List<LocItem> =
      locations.map { LocItem(it.id, it.name, it.country, it.lat, it.lon) }

  private fun remoteLocationsToLocItems(locations: List<Location>): List<LocItem> =
      locations.map { LocItem(it.id, it.name, it.sys.country, it.coord.lat, it.coord.lon) }


}