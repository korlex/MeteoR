package com.github.korlex.meteor.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.korlex.meteor.db.model.LocationDbModel
import io.reactivex.Single

@Dao
abstract class LocationDao {

  @Query("SELECT * FROM LocationDbModel")
  abstract fun getLocations(): Single<List<LocationDbModel>>

  @Query("DELETE FROM LocationDbModel")
  abstract fun removeLocations()

  @Insert
  abstract fun setLocations(locations: List<LocationDbModel>)

  @Transaction
  open fun replaceLocations(locations: List<LocationDbModel>) {
    removeLocations()
    setLocations(locations)
  }
}