package com.github.korlex.meteor.repo.weather

import com.github.korlex.meteor.api.MeteorService
import com.github.korlex.meteor.api.model.weather.WeatherResponse
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
    private val meteoPrefs: MeteorPrefs) : WeatherDataSource {

  private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM")

  override fun getLocation(): String = meteoPrefs.locName.get()

  override fun getWeather2(locale: String): Single<WeatherSchedule> =
      meteorService.getWeather(meteoPrefs.locId.get(), locale).map { forecastsToWeatherSchedule(it, locale) }

  private fun forecastsToWeatherSchedule(weatherResponse: WeatherResponse, locale: String): WeatherSchedule {
    val timeItems: MutableList<MutableList<TimeItem>> = mutableListOf()
    val dateItems: MutableList<DateItem> = mutableListOf()
    var datePrev: LocalDate = LocalDate.parse(weatherResponse.list.first().dtTxt, dateTimeFormatter)
    weatherResponse.list.forEach {
      val date = LocalDate.parse(it.dtTxt, dateTimeFormatter)
      val time = LocalTime.parse(it.dtTxt, dateTimeFormatter)
      val airPressure = convertPressure(it.main.pressure)
      val temperature = convertTemp(it.main.temp)
      val windSpeed = convertSpeed(it.wind.speed)
      val weatherState = WeatherState(it.weather.first().id, it.weather.first().description)
      val timeItem = TimeItem(time.hour, weatherState, temperature, airPressure, windSpeed, it.main.humidity)
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
    else -> throw IllegalArgumentException("Unknown pressure units")
  }

  private fun convertSpeed(value: Double): Int = when(meteoPrefs.speedUnit.get()) {
    MS   -> value.toInt()
    MH   -> (value * 2.237).toInt()
    else -> throw IllegalArgumentException("Unknown speed units")
  }

  private fun convertTemp(value: Double) = when(meteoPrefs.tempUnit.get()) {
    KELVIN     -> value.toInt()
    CELSIUS    -> (value - 273.15).toInt()
    FAHRENHEIT -> ((value - 273.15) * 9 / 5 + 32).toInt()
    else       -> throw IllegalArgumentException("Unknown temp units")
  }

}