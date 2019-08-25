package com.github.korlex.meteor.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.korlex.meteor.db.dao.ForecastDao
import com.github.korlex.meteor.db.dao.LocationDao
import com.github.korlex.meteor.db.model.ForecastDbModel
import com.github.korlex.meteor.db.model.LocationDbModel

private const val DB_NAME = "METEOR_DB"

@Database(entities = [LocationDbModel::class, ForecastDbModel::class], version = 1, exportSchema = false)
abstract class MeteorDb : RoomDatabase() {

  abstract fun locationDao(): LocationDao
  abstract fun forecastDao(): ForecastDao

  companion object {
    fun buildDb(context: Context): MeteorDb =
        Room.databaseBuilder(context, MeteorDb::class.java, DB_NAME).build()
  }
}