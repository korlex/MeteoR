package com.github.korlex.meteor.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.korlex.meteor.db.model.ForecastDbModel
import io.reactivex.Single

@Dao
abstract class ForecastDao {

  @Query("SELECT * FROM forecastDbModel")
  abstract fun getForecasts(): Single<List<ForecastDbModel>>

  @Query("DELETE FROM forecastDbModel")
  abstract fun removeForecasts()

  @Insert
  abstract fun setForecasts(forecasts: List<ForecastDbModel>)

  @Transaction
  open fun replaceForecasts(forecasts: List<ForecastDbModel>) {
    removeForecasts()
    setForecasts(forecasts)
  }
}