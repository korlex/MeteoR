package com.github.korlex.meteor.screen.weather

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.korlex.meteor.R
import com.github.korlex.meteor.extensions.inflate
import com.github.korlex.meteor.screen.weather.model.DateItem
import com.github.korlex.meteor.screen.weather.model.ForecastItem
import kotlinx.android.synthetic.main.item_date.view.tvDate
import kotlinx.android.synthetic.main.item_forecast.view.ivIcon
import kotlinx.android.synthetic.main.item_forecast.view.tvCondition
import kotlinx.android.synthetic.main.item_forecast.view.tvMaxTemp
import kotlinx.android.synthetic.main.item_forecast.view.tvMinTemp
import kotlinx.android.synthetic.main.item_forecast.view.tvTime
import org.threeten.bp.format.DateTimeFormatter

class WeatherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//  private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM")
//  private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm")
  var weatherItems: List<Any> = ArrayList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getItemViewType(position: Int): Int = when(weatherItems[position]) {
    is ForecastItem -> FORECAST
    is DateItem     -> DATE
    else            -> throw IllegalArgumentException("Wrong item type")
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = when(viewType) {
    FORECAST -> ForecastViewHolder(parent.inflate(R.layout.item_forecast, false))
    DATE     -> DateViewHolder(parent.inflate(R.layout.item_date, false))
    else     -> throw IllegalArgumentException("ViewHolder cant be created. Wrong item type")
  }

  override fun getItemCount(): Int = weatherItems.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    when(holder) {
      is ForecastViewHolder -> holder.bind(weatherItems[position] as ForecastItem)
      is DateViewHolder     -> holder.bind(weatherItems[position] as DateItem)
    }
  }

  private fun getIcon(iconId: Int): Int = when(iconId) {
    in 200..232 -> R.drawable.ic_thunderstorm
    in 300..321 -> R.drawable.ic_shower_rain
    in 500..504 -> R.drawable.ic_rain
    in 520..531 -> R.drawable.ic_shower_rain
    in 600..622 -> R.drawable.ic_snow
    in 701..781 -> R.drawable.ic_mist
    in 803..804 -> R.drawable.ic_broken_clouds

    511         -> R.drawable.ic_snow
    800         -> R.drawable.ic_clear_sky
    801         -> R.drawable.ic_few_clouds
    802         -> R.drawable.ic_scattered_clouds
    else        -> throw IllegalArgumentException("wrong iconId")
  }

  inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(dateItem: DateItem) {
      with(itemView) {
        with(dateItem) {
          tvDate.text = resources.getString(R.string.day_month, date.dayOfMonth, date.dayOfWeek.name)
        }
      }
    }
  }

  inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(forecastItem: ForecastItem) {
      with(itemView) {
        with(forecastItem) {
          tvTime.text = resources.getString(R.string.hours_minutes, time.hour, time.minute)
          tvCondition.text = resources.getString(R.string.condition, condition)
          tvMaxTemp.text = resources.getString(R.string.max_temp, maxTemp)
          tvMinTemp.text = resources.getString(R.string.min_temp, minTemp)
          ivIcon.setImageResource(getIcon(iconId))
        }
      }
    }
  }

  companion object {
    private const val FORECAST = 1
    private const val DATE = 0
  }
}