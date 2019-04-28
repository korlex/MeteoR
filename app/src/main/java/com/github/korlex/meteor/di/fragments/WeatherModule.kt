package com.github.korlex.meteor.di.fragments

import com.github.korlex.meteor.di.scopes.FragmentScope
import com.github.korlex.meteor.repo.weather.WeatherDataSource
import com.github.korlex.meteor.screen.weather.WeatherFragment
import com.github.korlex.meteor.screen.weather.WeatherPresenter
import com.github.korlex.meteor.screen.weather.WeatherView
import dagger.Module
import dagger.Provides

@Module
class WeatherModule {

  @Provides
  @FragmentScope
  fun provideWeatherView(weatherFragment: WeatherFragment): WeatherView = weatherFragment

  @Provides
  @FragmentScope
  fun provideWeatherPresenter(
      weatherView: WeatherView,
      weatherDataSource: WeatherDataSource): WeatherPresenter =
      WeatherPresenter(weatherView, weatherDataSource)
}