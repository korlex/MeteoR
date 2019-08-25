package com.github.korlex.meteor

import android.app.Activity
import android.app.Application
import com.github.korlex.meteor.di.app.DaggerAppComponent
import com.google.android.gms.maps.MapsInitializer
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MeteorApp : Application(), HasActivityInjector {

  @Inject
  lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  override fun onCreate() {
    super.onCreate()
    initDagger()
    MapsInitializer.initialize(this)
  }

  override fun activityInjector(): AndroidInjector<Activity> {
    return  activityDispatchingAndroidInjector
  }

  private fun initDagger(){
    DaggerAppComponent
        .builder()
        .application(this)
        .build()
        .inject(this)
  }
}