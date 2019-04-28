package com.github.korlex.meteor.di.app

import android.content.Context
import com.github.korlex.meteor.api.MeteorApi
import com.github.korlex.meteor.api.MeteorService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RetrofitModule {
  @Provides
  @Singleton
  fun provideDentapplService(context: Context): MeteorService {
    return MeteorApi.createMeteorService(context)
  }
}