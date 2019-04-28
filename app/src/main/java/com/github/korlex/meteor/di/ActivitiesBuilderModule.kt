package com.github.korlex.meteor.di

import com.github.korlex.meteor.MainActivity
import com.github.korlex.meteor.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivitiesBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentsBuilderModule::class])
    @ActivityScope
    fun contributeMainActivity(): MainActivity
}