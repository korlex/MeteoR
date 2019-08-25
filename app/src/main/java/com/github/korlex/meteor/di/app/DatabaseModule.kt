package com.github.korlex.meteor.di.app

import android.content.Context
import com.github.korlex.meteor.db.MeteorDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
  @Provides
  @Singleton
  fun provideMeteorDb(context: Context): MeteorDb {
    return MeteorDb.buildDb(context)
  }
}