package com.github.korlex.meteor.di.app

import android.app.Application
import android.content.Context
import com.github.korlex.meteor.preferences.MeteorPrefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

  @Provides
  @Singleton
  fun provideContext(application: Application) = application.applicationContext

  @Provides
  @Singleton
  fun providePreferences(context: Context) = MeteorPrefs(context)
}