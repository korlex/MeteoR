package com.github.korlex.meteor.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences

class MeteorPrefs(context: Context) {

  private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val rxPreferences: RxSharedPreferences = RxSharedPreferences.create(preferences)

  val location: Preference<String> = rxPreferences.getString(LOCATION)
  val lat: Preference<String> = rxPreferences.getString(LAT)
  val lon: Preference<String> = rxPreferences.getString(LON)

  fun isFilled(): Boolean = location.isSet && lat.isSet && lon.isSet

  companion object {
    const val LOCATION = "location"
    const val LAT = "lat"
    const val LON = "lon"
  }
}