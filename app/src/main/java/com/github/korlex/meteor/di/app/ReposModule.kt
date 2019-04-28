package com.github.korlex.meteor.di.app

import com.github.korlex.meteor.repo.location.LocationDataSource
import com.github.korlex.meteor.repo.location.LocationRepository
import com.github.korlex.meteor.repo.weather.WeatherDataSource
import com.github.korlex.meteor.repo.weather.WeatherRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ReposModule {

  @Binds
  @Singleton
  fun provideLocationDataSource(locationRepository: LocationRepository): LocationDataSource

  @Binds
  @Singleton
  fun provideWeatherDataSource(weatherRepository: WeatherRepository): WeatherDataSource

}