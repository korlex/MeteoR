package com.github.korlex.meteor.screen.weather


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout.LayoutParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.korlex.meteor.BaseFragment
import com.github.korlex.meteor.MainActivity
import com.github.korlex.meteor.R
import com.github.korlex.meteor.R.dimen
import com.github.korlex.meteor.exceptions.ForceLoadTimeThrowable
import com.github.korlex.meteor.extensions.fadeIn
import com.github.korlex.meteor.extensions.fadeOut
import com.github.korlex.meteor.extensions.getThemeColorResId
import com.github.korlex.meteor.extensions.setIcon
import com.github.korlex.meteor.preferences.MeteorPrefs
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.HPA
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.MH
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.MMHG
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.MS
import com.github.korlex.meteor.screen.settings.SettingsFragment
import com.github.korlex.meteor.screen.weather.model.DateItem
import com.github.korlex.meteor.screen.weather.model.TimeItem
import com.github.korlex.meteor.screen.weather.model.WeatherSchedule
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_weather.contentWrapper
import kotlinx.android.synthetic.main.fragment_weather.layoutWarn
import kotlinx.android.synthetic.main.fragment_weather.layoutWarnForceLoad
import kotlinx.android.synthetic.main.fragment_weather.pbLoading
import kotlinx.android.synthetic.main.fragment_weather.toolbar
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.layout_w_bottom2.*
import kotlinx.android.synthetic.main.layout_w_mid.rvTime
import kotlinx.android.synthetic.main.layout_w_top.ivIcon
import kotlinx.android.synthetic.main.layout_w_top.tvHumidity
import kotlinx.android.synthetic.main.layout_w_top.tvMainState
import kotlinx.android.synthetic.main.layout_w_top.tvMainTemp
import kotlinx.android.synthetic.main.layout_w_top.tvPressure
import kotlinx.android.synthetic.main.layout_w_top.tvSpeed
import kotlinx.android.synthetic.main.layout_w_top.weatherWrapper
import kotlinx.android.synthetic.main.layout_warn.btnWarnReload
import kotlinx.android.synthetic.main.layout_warn.tvWarnText
import kotlinx.android.synthetic.main.layout_warn_forceload.btnWarnForceLoadReload
import kotlinx.android.synthetic.main.layout_warn_forceload.tvWarnForceLoadText
import javax.inject.Inject

class WeatherFragment : BaseFragment(), WeatherView {

  @Inject lateinit var meteoPrefs: MeteorPrefs
  @Inject lateinit var weatherPresenter: WeatherPresenter
  private lateinit var airPressureUnits: String
  private lateinit var windSpeedUnits: String
  private lateinit var settingsDisposable: Disposable
  private val timeAdapter: TimeAdapter = TimeAdapter { pos, item -> onTimeSelected(pos, item) }

  private var selectedDatePosition: Int = 0
  private var selectedTimePosition: Int = 0

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    AndroidSupportInjection.inject(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    selectedDatePosition = arguments?.getInt(SELECTED_DATE) ?: 0
    subscribeOnUnits(meteoPrefs.pressureUnit.asObservable())
    subscribeOnUnits(meteoPrefs.speedUnit.asObservable())
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_weather, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupToolbar()
    setupContentWrapper()
    setupTimeList()
    setupWarnLayout()
    setupWarnForceLoadLayout()
    weatherPresenter.getWeather(getString(R.string.locale))
  }

  override fun onDestroy() {
    settingsDisposable.dispose()
    super.onDestroy()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(SELECTED_DATE, selectedDatePosition)
  }

  override fun showWeather(weatherSchedule: WeatherSchedule) {
    fillDatesSection2(weatherSchedule.dateItems)
    fillTimeSection(weatherSchedule.timeItems)
    onDateSelected(selectedDatePosition)
    contentWrapper.isRefreshing = false
    pbLoading.fadeOut()
    layoutWarn.fadeOut()
    contentWrapper.fadeIn()
  }

  override fun showProgress() {
    contentWrapper.fadeOut()
    layoutWarn.fadeOut()
    pbLoading.fadeIn()
  }

  override fun showError() {
    tvWarnText.text = getString(R.string.error_weather)
    contentWrapper.isRefreshing = false
    contentWrapper.fadeOut()
    pbLoading.fadeOut()
    layoutWarn.fadeIn()
  }

  override fun showErrorTimeForceLoad(throwable: ForceLoadTimeThrowable) {
    tvWarnForceLoadText.text = getString(R.string.warn_time_force_load, throwable.time)
    contentWrapper.isRefreshing = false
    contentWrapper.fadeOut()
    layoutWarnForceLoad.fadeIn()
  }

  override fun showErrorNetForceLoad() {
    tvWarnForceLoadText.text = getString(R.string.warn_net_force_load)
    contentWrapper.isRefreshing = false
    contentWrapper.fadeOut()
    layoutWarnForceLoad.fadeIn()  }

  private fun showSettings() {
    (fetchActivity() as MainActivity).replaceFragment(SettingsFragment(), true)
  }

  private fun setupToolbar() {
    with(toolbar) {
      setOptionalBtn2Listener { showSettings() }
      setTitle(weatherPresenter.getPlaceName())
    }
  }

  private fun setupContentWrapper() {
    contentWrapper.apply {
      setOnRefreshListener { weatherPresenter.getWeather(getString(R.string.locale), true) }
      setColorSchemeResources(fetchActivity().getThemeColorResId(R.attr.colorPrimaryDark))
    }
  }

  private fun setupTimeList() {
    with(rvTime) {
      layoutManager = LinearLayoutManager(fetchActivity(), LinearLayoutManager.HORIZONTAL, false)
      adapter = timeAdapter
    }
  }

  private fun setupWarnLayout() {
    btnWarnReload.setOnClickListener {
      weatherPresenter.getWeather(getString(R.string.locale))
    }
  }

  private fun setupWarnForceLoadLayout() {
    btnWarnForceLoadReload.setOnClickListener {
      layoutWarnForceLoad.fadeOut()
      contentWrapper.fadeIn()
    }
  }

  private fun subscribeOnUnits(source: Observable<String>) {
    settingsDisposable = source
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { unitCode -> applyUnitSettings(unitCode) }
  }

  private fun applyUnitSettings(unitCode: String) {
    when(unitCode) {
      HPA  -> airPressureUnits = getString(R.string.hpa)
      MMHG -> airPressureUnits = getString(R.string.mm_hg)
      MS   -> windSpeedUnits = getString(R.string.meter_sec)
      MH   -> windSpeedUnits = getString(R.string.miles_hour)
    }
  }

  private fun fillDatesSection2(dateItems: List<DateItem>) {
    dateItems.forEachIndexed { index, dateItem2 ->
      if(index > 0) {
        separateDateView()
        createDateView(index, dateItem2)
      } else {
        createDateView(index, dateItem2)
      }
    }
  }

  private fun fillTimeSection(timeItems: List<List<TimeItem>>) {
    timeAdapter.setupTimeRanges(timeItems)
  }

  private fun fillCurWeatherSection(timeItem: TimeItem) {
    with(timeItem) {
      tvPressure.text = getString(R.string.pressure_template, airPressure, airPressureUnits)
      tvSpeed.text = getString(R.string.wind_speed_template, windSpeed, windSpeedUnits)
      tvHumidity.text = getString(R.string.humidity_template, humidity)

      tvMainTemp.text = getString(R.string.temp, temp)
      tvMainState.text = state.stateTitle
      ivIcon.setIcon(state.stateId)
      weatherWrapper.fadeIn()
    }
  }

  private fun onDateSelected(datePosition: Int) {
    updateDateView(datesWrapper.getChildAt(selectedDatePosition), false)
    updateDateView(datesWrapper.getChildAt(datePosition), true)
    timeAdapter.setupTimeRange(datePosition / 2)
    selectedDatePosition = datePosition
  }

  private fun onTimeSelected(position: Int, timeItem: TimeItem) {
    weatherWrapper.fadeOut()
    fillCurWeatherSection(timeItem)
    selectedTimePosition = position
  }

  private fun createDateView(index: Int, dateItem: DateItem) {
    val view = LayoutInflater.from(fetchActivity()).inflate(R.layout.item_date, datesWrapper, false).apply {
      with(dateItem) {
        val strStartTime = getString(R.string.hour, startHour)
        val strEndTime = getString(R.string.hour, endHour)
        tvDateTemp.text = getString(R.string.temp_state, avgTemp, avgState.stateTitle)
        tvDate.text = getString(R.string.date_time, date, strStartTime, strEndTime)
        ivDateIcon.setIcon(avgState.stateId)
        setOnClickListener { onDateSelected(index * 2) }
      }
    }

    datesWrapper.addView(view)
  }

  private fun updateDateView(view: View, selected: Boolean) {
    with(view) {
      if(selected) {
        ivDateIcon.isSelected = true
        tvDateTemp.isSelected = true
        tvDate.isSelected = true

      } else {
        ivDateIcon.isSelected = false
        tvDateTemp.isSelected = false
        tvDate.isSelected = false
      }
    }
  }

  private fun separateDateView() {
    val view = View(fetchActivity()).apply {
      layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(dimen.default_separator_size))
      (layoutParams as MarginLayoutParams).marginStart = resources.getDimensionPixelSize(R.dimen.indent_small)
      (layoutParams as MarginLayoutParams).marginEnd = resources.getDimensionPixelSize(R.dimen.indent_small)
      setBackgroundColor(ContextCompat.getColor(context, R.color.gray_b3b3b3))
    }

    datesWrapper.addView(view)
  }


  companion object { private const val SELECTED_DATE = "selected_date" }
}
