package com.github.korlex.meteor.repo.location

import com.github.korlex.meteor.api.MeteorService
import com.github.korlex.meteor.api.model.location.LocationResponse
import com.github.korlex.meteor.preferences.MeteorPrefs
import com.github.korlex.meteor.screen.settings.location.model.LocItem
import io.reactivex.Single
import javax.inject.Inject

class LocationRepository @Inject constructor (
    private val meteorService: MeteorService,
    private val meteoPrefs: MeteorPrefs) : LocationDataSource {

  override fun getLocations(q: String?): Single<List<LocItem>> =
      meteorService.getLocations(q).map { locResponseToLocItems(it) }

  override fun saveLocation(loc: String, lat: String, lon: String) {
    meteoPrefs.location.set(loc)
    meteoPrefs.lat.set(lat)
    meteoPrefs.lon.set(lon)
  }

  private fun locResponseToLocItems(locationResponse: LocationResponse): List<LocItem> =
      locationResponse.list.map { LocItem(it.name, it.sys.country, it.coord.lat, it.coord.lon) }
}