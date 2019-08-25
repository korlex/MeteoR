package com.github.korlex.meteor.api

import com.github.korlex.meteor.api.MeteorApi.API_KEY_QUERY
import com.github.korlex.meteor.api.MeteorApi.UNITS_METRIC
import com.github.korlex.meteor.api.model.location.LocationResponse
import com.github.korlex.meteor.api.model.weather.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MeteorService {

  @GET("data/2.5/find?$API_KEY_QUERY")
  fun getLocationsByQuery(@Query("q") loc: String?): Single<LocationResponse>

  @GET("data/2.5/find?$API_KEY_QUERY")
  fun getLocationsByCoord(
      @Query("lat") lat: Double,
      @Query("lon") lon: Double): Single<LocationResponse>

  @GET("data/2.5/forecast?$API_KEY_QUERY")
  fun getWeather(
      @Query("id") id: Int,
      @Query("lang") lang: String): Single<WeatherResponse>
}