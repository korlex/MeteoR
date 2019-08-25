package com.github.korlex.meteor.repo.weather

import com.github.korlex.meteor.api.MeteorService
import com.github.korlex.meteor.api.model.weather.WeatherResponse
import com.github.korlex.meteor.db.MeteorDb
import com.github.korlex.meteor.db.model.ForecastDbModel
import com.github.korlex.meteor.exceptions.ForceLoadNetThrowable
import com.github.korlex.meteor.exceptions.ForceLoadTimeThrowable
import com.github.korlex.meteor.preferences.MeteorPrefs
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.CELSIUS
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.FAHRENHEIT
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.HPA
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.KELVIN
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.MH
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.MMHG
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.MS
import com.github.korlex.meteor.screen.weather.model.*
import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class WeatherRepository @Inject constructor (
    private val meteorService: MeteorService,
    private val meteoPrefs: MeteorPrefs,
    private val meteorDb: MeteorDb) : WeatherDataSource {

  private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM")
  private var remainTimeToLoad: Long = NOT_SET

  override fun getLocation(): String = meteoPrefs.locName.get()

  override fun getWeather(locale: String, forceLoad: Boolean): Single<WeatherSchedule> =
      pickDataSource(meteoPrefs.locId.get(), locale, forceLoad).map { forecastsToWeatherSchedule(it, locale) }

  private fun pickDataSource(locationId: Int, locale: String, forceLoad: Boolean): Single<List<ForecastDbModel>> = when {
    checkLocationChange(locationId) -> getUpdatedLocalData(locationId, locale)
    checkLanguageChange(locale)     -> getUpdatedLocalData(locationId, locale)
    checkLastUpdateTime()           -> getCurrentLocalData(forceLoad)
    else                            -> getData(locationId, locale, forceLoad)
  }

  private fun checkLanguageChange(locale: String): Boolean = locale != meteoPrefs.loadLang.get()

  private fun checkLocationChange(locId: Int): Boolean = locId != meteoPrefs.loadLocId.get()

  private fun checkLastUpdateTime(): Boolean {
    remainTimeToLoad = (TEN_MINUTES_IN_MS - (System.currentTimeMillis() - meteoPrefs.loadTime.get())) / ONE_MINUTE_IN_MS
    return remainTimeToLoad > 0
  }

  private fun getUpdatedLocalData(locId: Int, locale: String): Single<List<ForecastDbModel>> =
      meteorService
          .getWeather(locId, locale)
          .doOnSuccess {
            saveRemoteLoadParams(locId, locale)
            saveForecasts(it)
          }
          .flatMap { meteorDb.forecastDao().getForecasts() }

  private fun getCurrentLocalData(forceLoad: Boolean): Single<List<ForecastDbModel>> =
      meteorDb
          .forecastDao()
          .getForecasts()
          .doOnSuccess { if(forceLoad) throw ForceLoadTimeThrowable(remainTimeToLoad) }

  private fun getData(locId: Int, locale: String, forceLoad: Boolean): Single<List<ForecastDbModel>> =
      getUpdatedLocalData(locId, locale)
          .onErrorResumeNext { if(!forceLoad) getCurrentLocalData(false) else throw ForceLoadNetThrowable() }

  private fun saveForecasts(weatherResponse: WeatherResponse) {
    meteorDb.forecastDao().replaceForecasts(weatherResponse.list.map {
      ForecastDbModel(
          it.dtTxt,
          it.dt,
          it.main.humidity,
          it.main.pressure,
          it.main.temp,
          it.weather.first().description,
          it.weather.first().icon,
          it.weather.first().id,
          it.weather.first().main,
          it.wind.speed)
    })
  }

  private fun saveRemoteLoadParams(locId: Int, locale: String) {
    meteoPrefs.loadLocId.set(locId)
    meteoPrefs.loadTime.set(System.currentTimeMillis())
    meteoPrefs.loadLang.set(locale)
  }

  private fun forecastsToWeatherSchedule(forecasts: List<ForecastDbModel>, locale: String): WeatherSchedule {
    val timeItems: MutableList<MutableList<TimeItem>> = mutableListOf()
    val dateItems: MutableList<DateItem> = mutableListOf()
    var datePrev: LocalDate = LocalDate.parse(forecasts.first().dtTxt, dateTimeFormatter)
    forecasts.forEach {
      val date = LocalDate.parse(it.dtTxt, dateTimeFormatter)
      val time = LocalTime.parse(it.dtTxt, dateTimeFormatter)
      val airPressure = convertPressure(it.mainPressure)
      val temperature = convertTemp(it.mainTemp)
      val windSpeed = convertSpeed(it.windSpeed)
      val weatherState = WeatherState(it.weatherId, it.weatherDesc)
      val timeItem = TimeItem(time.hour, weatherState, temperature, airPressure, windSpeed, it.mainHumidity)
      if (date != datePrev) {
        dateItems.add(createDateItem(datePrev, timeItems.last(), locale))
        timeItems.add(mutableListOf(timeItem))
        datePrev = date

      } else if(!timeItems.isEmpty()){
        timeItems.last().add(timeItem)
      } else {
        timeItems.add(mutableListOf(timeItem))
      }
    }

    dateItems.add(createDateItem(datePrev, timeItems.last(), locale))
    return WeatherSchedule(dateItems, timeItems)
  }

  private fun createDateItem(date: LocalDate, dayTimes: List<TimeItem>, locale: String): DateItem {
    val dateString = date.format(dateFormatter.withLocale(Locale(locale)))
    val startDayHour = dayTimes.first().hour
    val closeDayHour = dayTimes.last().hour.plus(3)
    val avgTemp = dayTimes.map { item -> item.temp }.sum() / dayTimes.size
    val avgWeatherState = dayTimes
        .groupingBy { item -> item.state }
        .eachCount()
        .maxBy { stateAmount -> stateAmount.value }
        ?.key ?: throw IllegalArgumentException("avgWeatherState must not be null")

    return DateItem(dateString, startDayHour, closeDayHour, avgWeatherState, avgTemp)
  }

  private fun convertPressure(value: Double): Int = when(meteoPrefs.pressureUnit.get()) {
    HPA  -> value.toInt()
    MMHG -> (value / 1.333).toInt()
    else -> throw IllegalArgumentException("Unknown mainPressure units")
  }

  private fun convertSpeed(value: Double): Int = when(meteoPrefs.speedUnit.get()) {
    MS   -> value.toInt()
    MH   -> (value * 2.237).toInt()
    else -> throw IllegalArgumentException("Unknown windSpeed units")
  }

  private fun convertTemp(value: Double) = when(meteoPrefs.tempUnit.get()) {
    KELVIN     -> value.toInt()
    CELSIUS    -> (value - 273.15).toInt()
    FAHRENHEIT -> ((value - 273.15) * 9 / 5 + 32).toInt()
    else       -> throw IllegalArgumentException("Unknown mainTemp units")
  }

  companion object {
   private const val TEN_MINUTES_IN_MS = 600000L
   private const val ONE_MINUTE_IN_MS = 60000L
   private const val NOT_SET = -1L
  }
}