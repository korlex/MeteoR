package com.github.korlex.meteor.repo.location

import com.github.korlex.meteor.api.model.location.Location
import com.github.korlex.meteor.screen.settings.location.model.LocItem
import io.reactivex.Single

interface LocationDataSource {
  fun getLocationsByCoord(lat: Double, lon: Double): Single<List<LocItem>>
  fun getLocationsByQuery(q: String?): Single<List<LocItem>>
  fun getLocationsFromDb(): Single<List<LocItem>>
  fun savePickedLocation(placeId: Int, placeName: String, placePosition: Int)
  fun saveSearchedLocations(locations: List<LocItem>)
}