package com.github.korlex.meteor.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences

class MeteorPrefs(context: Context) {

  private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val rxPreferences: RxSharedPreferences = RxSharedPreferences.create(preferences)

  val locName: Preference<String> = rxPreferences.getString(LOCATION_NAME)
  val locPos: Preference<Int> = rxPreferences.getInteger(LOCATION_POS)
  val locId: Preference<Int> = rxPreferences.getInteger(LOCATION_ID)

  val pressureUnit: Preference<String> = rxPreferences.getString(PRESSURE_UNIT, HPA)
  val speedUnit: Preference<String> = rxPreferences.getString(SPEED_UNIT, MS)
  val tempUnit: Preference<String> = rxPreferences.getString(TEMP_UNIT, KELVIN)

  val colorScheme: Preference<String> = rxPreferences.getString(COLOR_SCHEME, OCEAN_BLUE)

  val loadLocId: Preference<Int> = rxPreferences.getInteger(LOAD_LOC_ID)
  val loadTime: Preference<Long> = rxPreferences.getLong(UPDATE_TIME)
  val loadLang: Preference<String> = rxPreferences.getString(LANGUAGE)


//  fun isFilled(): Boolean = locName.isSet && lat.isSet && lon.isSet

  companion object {
    const val LOCATION_NAME = "location_name"
    const val LOCATION_POS = "location_pos"
    const val LOCATION_ID = "location_id"


    const val PRESSURE_UNIT = "pressure_unit"
    const val MMHG = "mmhg"
    const val HPA = "hpa"

    const val SPEED_UNIT = "speed_unit"
    const val MS = "ms"
    const val MH = "mh"

    const val TEMP_UNIT = "temp_unit"
    const val FAHRENHEIT = "fahrenheit"
    const val CELSIUS = "celsius"
    const val KELVIN = "kelvin"

    const val COLOR_SCHEME = "color_scheme"
    const val OCEAN_BLUE = "ocean_blue"
    const val FOREST_GREEN = "forest_green"
    const val SUNSET_RED = "sunset_red"

    const val UPDATE_TIME = "update_time"
    const val LOAD_LOC_ID = "load_loc_id"
    const val LANGUAGE = "loadLang"
  }
}