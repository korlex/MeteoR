package com.github.korlex.meteor.screen.settings.measurement


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.korlex.meteor.BaseFragment

import com.github.korlex.meteor.R
import com.github.korlex.meteor.preferences.MeteorPrefs
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_measurement.pressureWrapper
import kotlinx.android.synthetic.main.fragment_measurement.speedWrapper
import kotlinx.android.synthetic.main.fragment_measurement.tempWrapper
import kotlinx.android.synthetic.main.fragment_measurement.toolbar
import javax.inject.Inject

class MeasurementFragment : BaseFragment() {

  @Inject lateinit var meteoPrefs: MeteorPrefs
  private lateinit var selectedPressure: String
  private lateinit var selectedSpeed: String
  private lateinit var selectedTemp: String

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    AndroidSupportInjection.inject(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    selectedPressure = meteoPrefs.pressureUnit.get()
    selectedSpeed = meteoPrefs.speedUnit.get()
    selectedTemp = meteoPrefs.tempUnit.get()
  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_measurement, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    toolbar.setNavBtnListener { fetchActivity().onBackPressed() }
    setupPressureUnits()
    setupSpeedUnits()
    setupTempUnits()
  }


  private fun setupTempUnits() {
    for(position in 0 until tempWrapper.childCount) {
      with(tempWrapper.getChildAt(position) as TextView) {
        if(tag == selectedTemp) updateUnitView(this, true)
        setOnClickListener { onTempSelected(tag as String) }
      }
    }
  }

  private fun onTempSelected(tag: String) {
    updateUnitView(tempWrapper.findViewWithTag(selectedTemp), false)
    updateUnitView(tempWrapper.findViewWithTag(tag), true)
    meteoPrefs.tempUnit.set(tag)
    selectedTemp = tag
  }

  private fun setupPressureUnits() {
    for (position in 0 until pressureWrapper.childCount) {
      with(pressureWrapper.getChildAt(position) as TextView) {
        if(tag == selectedPressure) updateUnitView(this, true)
        setOnClickListener { onPressureSelected(tag as String) }
      }
    }
  }

  private fun onPressureSelected(tag: String) {
    updateUnitView(pressureWrapper.findViewWithTag(selectedPressure), false)
    updateUnitView(pressureWrapper.findViewWithTag(tag), true)
    meteoPrefs.pressureUnit.set(tag)
    selectedPressure = tag
  }

  private fun setupSpeedUnits() {
    for (position in 0 until speedWrapper.childCount) {
      with(speedWrapper.getChildAt(position) as TextView) {
        if(tag == selectedSpeed) updateUnitView(this, true)
        setOnClickListener { onSpeedSelected(tag as String) }
      }
    }
  }

  private fun onSpeedSelected(tag: String) {
    updateUnitView(speedWrapper.findViewWithTag(selectedSpeed), false)
    updateUnitView(speedWrapper.findViewWithTag(tag), true)
    meteoPrefs.speedUnit.set(tag)
    selectedSpeed = tag
  }

  private fun updateUnitView(textView: TextView, selected: Boolean) {
    textView.isSelected = selected
  }
}
