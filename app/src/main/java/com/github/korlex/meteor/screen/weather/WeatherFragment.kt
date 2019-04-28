package com.github.korlex.meteor.screen.weather


import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.korlex.meteor.BaseFragment
import com.github.korlex.meteor.MainActivity

import com.github.korlex.meteor.R
import com.github.korlex.meteor.extensions.fadeIn
import com.github.korlex.meteor.extensions.fadeOut
import com.github.korlex.meteor.screen.settings.SettingsFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_weather.layoutMessage
import kotlinx.android.synthetic.main.fragment_weather.pbLoading
import kotlinx.android.synthetic.main.fragment_weather.rvForecasts
import kotlinx.android.synthetic.main.fragment_weather.toolbar
import kotlinx.android.synthetic.main.layout_message.btnMsgReload
import kotlinx.android.synthetic.main.layout_message.tvMsgTitle
import javax.inject.Inject

class WeatherFragment : BaseFragment(), WeatherView {

  @Inject lateinit var weatherPresenter: WeatherPresenter
  private val weatherAdapter: WeatherAdapter = WeatherAdapter()

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    AndroidSupportInjection.inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_weather, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpToolbar()
    setUpList()
    setUpProgress()
    setUpLayoutMessage()
    weatherPresenter.getWeather()
  }

  override fun showWeather(items: List<Any>) {
    weatherAdapter.weatherItems = items
    pbLoading.fadeOut()
    layoutMessage.fadeOut()
    rvForecasts.fadeIn()
  }

  override fun showProgress() {
    layoutMessage.fadeOut()
    rvForecasts.fadeOut()
    pbLoading.fadeIn()
  }

  override fun showEmpty() {
    tvMsgTitle.text = getString(R.string.forecasts_empty)
    pbLoading.fadeOut()
    rvForecasts.fadeOut()
    layoutMessage.fadeIn()
  }

  override fun showError() {
    tvMsgTitle.text = getString(R.string.forecasts_error)
    pbLoading.fadeOut()
    rvForecasts.fadeOut()
    layoutMessage.fadeIn()
  }

  private fun showSettings() {
    (fetchActivity() as MainActivity).replaceFragment(SettingsFragment(), true)
  }

  private fun setUpToolbar() {
    with(toolbar) {
      setTitle(weatherPresenter.getPlaceName())
      setOptionalBtnListener { showSettings() }
    }
  }

  private fun setUpList() {
    with(rvForecasts) {
      layoutManager = LinearLayoutManager(fetchActivity())
      adapter = weatherAdapter
    }
  }

  fun setUpProgress() {
    pbLoading.indeterminateDrawable.setColorFilter(
        ContextCompat.getColor(fetchActivity(), R.color.colorPrimaryDark),
        PorterDuff.Mode.SRC_IN)
  }

  private fun setUpLayoutMessage() {
    btnMsgReload.setOnClickListener {
      weatherPresenter.getWeather()
    }
  }
}
