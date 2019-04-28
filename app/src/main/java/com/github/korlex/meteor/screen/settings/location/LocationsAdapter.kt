package com.github.korlex.meteor.screen.settings.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.korlex.meteor.R
import com.github.korlex.meteor.screen.settings.location.model.LocItem
import kotlinx.android.synthetic.main.item_location.view.tvCoords
import kotlinx.android.synthetic.main.item_location.view.tvTitle

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>() {

  var locItems: List<LocItem> = ArrayList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  var selectedItem: LocItem? = null


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
    val v = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
    return LocationViewHolder(v).apply {
      itemView.setOnClickListener {
        selectedItem = locItems[adapterPosition]
        notifyDataSetChanged()
      }
    }
  }

  override fun getItemCount(): Int = locItems.size

  override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
    holder.bind(locItems[position])
  }

  inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun bind(locItem: LocItem) = with(itemView) {
      with(locItem) {
        tvTitle.text = resources.getString(R.string.loc_title, city, country)
        tvCoords.text = resources.getString(R.string.loc_coords, latitude, longitude)

        if(selectedItem == locItem) {
          tvTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
          tvCoords.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        } else {
          tvTitle.setTextColor(ContextCompat.getColor(context, R.color.gray_4d4d4d))
          tvCoords.setTextColor(ContextCompat.getColor(context, R.color.gray_4d4d4d))
        }
      }
    }
  }
}