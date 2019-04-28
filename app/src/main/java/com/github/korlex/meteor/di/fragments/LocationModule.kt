package com.github.korlex.meteor.di.fragments

import com.github.korlex.meteor.di.scopes.FragmentScope
import com.github.korlex.meteor.repo.location.LocationDataSource
import com.github.korlex.meteor.screen.settings.location.LocationFragment
import com.github.korlex.meteor.screen.settings.location.LocationPresenter
import com.github.korlex.meteor.screen.settings.location.LocationView
import dagger.Module
import dagger.Provides

@Module
class LocationModule {

    @Provides
    @FragmentScope
    fun provideLocationView(locationFragment: LocationFragment): LocationView = locationFragment

    @Provides
    @FragmentScope
    fun provideLocationPresenter(
        locationView: LocationView,
        locationDataSource: LocationDataSource): LocationPresenter =
        LocationPresenter(locationView, locationDataSource)
}