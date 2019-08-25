package com.github.korlex.meteor.screen.settings.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.korlex.meteor.R
import com.github.korlex.meteor.screen.settings.location.model.LocItem
import kotlinx.android.synthetic.main.item_location.view.ivIcon
import kotlinx.android.synthetic.main.item_location.view.tvCoords
import kotlinx.android.synthetic.main.item_location.view.tvTitle

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>() {

  private var locItems: List<LocItem> = ArrayList()
  private var pickedPos: Int = -1

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
    val v = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
    return LocationViewHolder(v).apply {
      itemView.setOnClickListener {
        pickedPos = adapterPosition
        notifyDataSetChanged()
      }
    }
  }

  override fun getItemCount(): Int = locItems.size

  override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
    holder.bind(locItems[position])
  }

  fun setData(locItems: List<LocItem>, pickedPos: Int? = null) {
    this.pickedPos = pickedPos ?: -1
    this.locItems = locItems
    notifyDataSetChanged()
  }

  fun getData(): Pair<List<LocItem>, Int> = Pair(locItems, pickedPos)

  inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(locItem: LocItem) = with(itemView) {
      with(locItem) {
        tvTitle.text = resources.getString(R.string.loc_title, placeName, country)
        tvCoords.text = resources.getString(R.string.loc_coords, latitude, longitude)

        if(pickedPos != -1) {
          if(locItem == locItems[pickedPos]) {
            ivIcon.isSelected = true
            tvTitle.isSelected = true
            tvCoords.isSelected = true
          } else {
            ivIcon.isSelected = false
            tvTitle.isSelected = false
            tvCoords.isSelected = false
          }
        }
      }
    }
  }
}