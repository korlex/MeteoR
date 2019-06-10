package com.github.korlex.meteor.screen.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.korlex.meteor.R
import com.github.korlex.meteor.extensions.setIcon
import com.github.korlex.meteor.screen.weather.TimeAdapter.TimeViewHolder
import com.github.korlex.meteor.screen.weather.model.TimeItem
import kotlinx.android.synthetic.main.item_time.view.*

class TimeAdapter(private val timePickCallback: (position: Int, timeItem: TimeItem) -> Unit) : RecyclerView.Adapter<TimeViewHolder>() {

  var timeRanges: List<List<TimeItem>> = ArrayList()
  var timeRange: List<TimeItem> = ArrayList()
  var selectedTimePosition: Int = 0

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
    val v = LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent, false)
    return TimeViewHolder(v).apply {
      itemView.setOnClickListener {
        selectedTimePosition = adapterPosition
        timePickCallback.invoke(adapterPosition, timeRange[adapterPosition])
        notifyDataSetChanged()
      }
    }
  }

  override fun getItemCount(): Int = timeRange.size

  override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
    holder.bind(timeRange[position])
  }

  fun setupTimeRanges(timeRanges: List<List<TimeItem>>) {
    this.timeRanges = timeRanges
    setupTimeRange(0)
  }


  fun setupTimeRange(timeRangePosition: Int) {
    timeRange = timeRanges[timeRangePosition]
    selectedTimePosition = 0
    timePickCallback.invoke(0, timeRange[0])
    notifyDataSetChanged()
  }

  inner class TimeViewHolder(itemView: View) : ViewHolder(itemView) {
    fun bind(timeItem: TimeItem) = with(itemView) {
      with(timeItem) {
        tvTime.text = resources.getString(R.string.hour, hour)
        tvTimeTemp.text = resources.getString(R.string.temp, temp)
        ivTimeIcon.setIcon(state.stateId)
        if(selectedTimePosition == adapterPosition) {
          ivTimeIcon.isSelected = true
          tvTimeTemp.isSelected = true
          tvTime.isSelected = true
        } else {
          ivTimeIcon.isSelected = false
          tvTimeTemp.isSelected = false
          tvTime.isSelected = false
        }
      }
    }
  }
}