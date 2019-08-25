package com.github.korlex.meteor.di.app

import android.app.Application
import com.github.korlex.meteor.MeteorApp
import com.github.korlex.meteor.di.ActivitiesBuilderModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [
    AppModule::class,
    RetrofitModule::class,
    DatabaseModule::class,
    ReposModule::class,
    ActivitiesBuilderModule::class,
    AndroidSupportInjectionModule::class,
    AndroidInjectionModule::class])
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(meteorApp: MeteorApp)
}