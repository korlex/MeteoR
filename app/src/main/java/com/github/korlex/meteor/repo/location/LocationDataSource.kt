package com.github.korlex.meteor.repo.location

import com.github.korlex.meteor.screen.settings.location.model.LocItem
import io.reactivex.Single

interface LocationDataSource {
  fun getLocations(q: String?): Single<List<LocItem>>
  fun saveLocation(placeId: Int, placeName: String)
}