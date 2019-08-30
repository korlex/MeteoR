package com.github.korlex.meteor.api

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MeteorApi {

  const val BASE_URL = "http://api.openweathermap.org/"
  const val API_KEY_QUERY = "appid=d3cc778955204610e79d2e2517e3a453"

  fun createMeteorService(context: Context): MeteorService {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(ChuckInterceptor(context))
        .build()

    val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    return retrofit.create(MeteorService::class.java)
  }
}