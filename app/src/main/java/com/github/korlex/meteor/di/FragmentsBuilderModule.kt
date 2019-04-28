package com.github.korlex.meteor.di

import com.github.korlex.meteor.di.fragments.LocationModule
import com.github.korlex.meteor.di.fragments.WeatherModule
import com.github.korlex.meteor.di.scopes.FragmentScope
import com.github.korlex.meteor.screen.settings.location.LocationFragment
import com.github.korlex.meteor.screen.weather.WeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentsBuilderModule {

  @ContributesAndroidInjector(modules = [LocationModule::class])
  @FragmentScope
  fun contributeLocationFragment(): LocationFragment

  @ContributesAndroidInjector(modules = [WeatherModule::class])
  @FragmentScope
  fun contributeWeatherFragment(): WeatherFragment

}