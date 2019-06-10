package com.github.korlex.meteor.screen.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.korlex.meteor.BaseFragment
import com.github.korlex.meteor.MainActivity

import com.github.korlex.meteor.R
import com.github.korlex.meteor.screen.settings.about.AboutFragment
import com.github.korlex.meteor.screen.settings.color.ColorFragment
import com.github.korlex.meteor.screen.settings.location.LocationFragment
import com.github.korlex.meteor.screen.settings.measurement.MeasurementFragment
import kotlinx.android.synthetic.main.fragment_settings.btnAbout
import kotlinx.android.synthetic.main.fragment_settings.btnColor
import kotlinx.android.synthetic.main.fragment_settings.btnLocation
import kotlinx.android.synthetic.main.fragment_settings.btnMeasurement
import kotlinx.android.synthetic.main.fragment_settings.toolbar

class SettingsFragment : BaseFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_settings, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    toolbar.setNavBtnListener { fetchActivity().onBackPressed() }
    btnLocation.setOnClickListener { showLocationsFragment() }
    btnMeasurement.setOnClickListener { showMeasurementFragment() }
    btnColor.setOnClickListener { showColorFragment() }
    btnAbout.setOnClickListener { showAboutFragment() }
  }

  private fun showLocationsFragment() {
    (activity as? MainActivity)?.replaceFragment(LocationFragment.create(false), true)
  }

  private fun showMeasurementFragment() {
    (activity as? MainActivity)?.replaceFragment(MeasurementFragment(), true)
  }

  private fun showColorFragment() {
    (activity as? MainActivity)?.replaceFragment(ColorFragment(), true)
  }

  private fun showAboutFragment() {
    (activity as? MainActivity)?.replaceFragment(AboutFragment(), true)
  }
}
