package com.github.korlex.meteor.repo.weather

import com.github.korlex.meteor.api.MeteorService
import com.github.korlex.meteor.api.model.weather.WeatherResponse
import com.github.korlex.meteor.preferences.MeteorPrefs
import com.github.korlex.meteor.screen.weather.model.DateItem
import com.github.korlex.meteor.screen.weather.model.ForecastItem
import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class WeatherRepository @Inject constructor (
    private val meteorService: MeteorService,
    private val meteoPrefs: MeteorPrefs) : WeatherDataSource {

  private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  override fun getWeather(): Single<List<Any>> =
      meteorService.getWeather(meteoPrefs.lat.get(), meteoPrefs.lon.get()).map { weatherResponseToWeatherItems(it) }

  override fun getLocation(): String = meteoPrefs.location.get()

  private fun weatherResponseToWeatherItems(weatherResponse: WeatherResponse): List<Any> {
    val weatherItems: MutableList<Any> = mutableListOf()
    var lastItemDate: LocalDate? = null
    weatherResponse.list.forEach {
      val itemDate = LocalDate.parse(it.dtTxt, dateTimeFormatter)
      val itemTime = LocalTime.parse(it.dtTxt, dateTimeFormatter)

      if(itemDate != lastItemDate) {
        weatherItems.add(DateItem(itemDate))
        lastItemDate = itemDate
      }
      weatherItems.add(ForecastItem(
          itemTime,
          it.weather.first().description,
          it.weather.first().id,
          it.main.tempMax,
          it.main.tempMin))
    }
    return weatherItems
  }
}