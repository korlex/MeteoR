package com.github.korlex.meteor.screen.settings.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.korlex.meteor.BaseFragment
import com.github.korlex.meteor.MainActivity

import com.github.korlex.meteor.R
import com.github.korlex.meteor.extensions.fadeIn
import com.github.korlex.meteor.extensions.fadeOut
import com.github.korlex.meteor.preferences.MeteorPrefs
import com.github.korlex.meteor.screen.settings.location.model.LocItem
import com.github.korlex.meteor.utils.NetworkManager
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_location.btnApply
import kotlinx.android.synthetic.main.fragment_location.layoutPlaceholder
import kotlinx.android.synthetic.main.fragment_location.layoutWarn
import kotlinx.android.synthetic.main.fragment_location.pbLoading
import kotlinx.android.synthetic.main.fragment_location.rvLocations
import kotlinx.android.synthetic.main.fragment_location.searchToolbar
import kotlinx.android.synthetic.main.layout_warn.btnWarnReload
import kotlinx.android.synthetic.main.layout_warn.tvWarnText
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class LocationFragment : BaseFragment(), LocationListener, LocationView {

  @Inject lateinit var meteorPrefs: MeteorPrefs
  @Inject lateinit var locPresenter: LocationPresenter
  @Inject lateinit var networkManager: NetworkManager
  private lateinit var locManager: LocationManager
  private lateinit var geocoder: Geocoder
  private val locAdapter: LocationsAdapter = LocationsAdapter()
  private var initialSetup: Boolean = false
  private val criteria: Criteria = Criteria().apply {
    horizontalAccuracy = Criteria.ACCURACY_HIGH
    verticalAccuracy = Criteria.ACCURACY_HIGH
    accuracy = Criteria.ACCURACY_COARSE
    powerRequirement = Criteria.POWER_LOW
    isAltitudeRequired = false
    isBearingRequired = false
    isSpeedRequired = false
    isCostAllowed = true
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    AndroidSupportInjection.inject(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initialSetup = arguments?.getBoolean(INITIAL_SETUP) ?: throw IllegalArgumentException("initial setup param must not be null")
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_location, container, false)

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    locManager = fetchActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    geocoder = Geocoder(fetchActivity(), Locale(getString(R.string.locale)))
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpToolbar()
    setUpList()
    setUpMessage()
    btnApply.setOnClickListener { onLocationApplied() }
    if(!initialSetup) showPredefinedResult()

  }

  override fun onLocationChanged(location: Location) {
    searchToolbar.setQuery(geocoder.getFromLocation(location.latitude, location.longitude,1)[0].locality)
  }

  override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    Timber.d("status changed, status: $status")
  }

  override fun onProviderEnabled(provider: String) {
    Timber.d("provider enabled, provider: $provider")
  }

  override fun onProviderDisabled(provider: String) {
    Timber.d("provider disabled, provider: $provider")
  }

  override fun showLocationsRemote(locItems: List<LocItem>) {
    locAdapter.setData(locItems)
    pbLoading.fadeOut()
    layoutWarn.fadeOut()
    layoutPlaceholder.fadeOut()
    rvLocations.fadeIn()
    btnApply.fadeIn()
  }

  override fun showLocationsDb(locItems: List<LocItem>) {
    locAdapter.setData(locItems, meteorPrefs.locPos.get())
    pbLoading.fadeOut()
    layoutWarn.fadeOut()
    layoutPlaceholder.fadeOut()
    rvLocations.fadeIn()
    btnApply.fadeIn()
  }

  override fun showProgress() {
    layoutWarn.fadeOut()
    layoutPlaceholder.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    pbLoading.fadeIn()
  }

  override fun showEmpty() {
    tvWarnText.text = getString(R.string.locations_empty)
    btnWarnReload.visibility = View.GONE
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutPlaceholder.fadeOut()
    layoutWarn.fadeIn()
  }

  override fun showError() {
    tvWarnText.text = getString(R.string.locations_error)
    btnWarnReload.visibility = View.VISIBLE
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutPlaceholder.fadeOut()
    layoutWarn.fadeIn()
  }

  private fun showPlaceHolder() {
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutWarn.fadeOut()
    layoutPlaceholder.fadeIn()
  }

  private fun showToast(msg: String) {
    Toast.makeText(fetchActivity(), msg, Toast.LENGTH_SHORT).show()
  }

  private fun showPredefinedResult() {
    searchToolbar.setQuery(meteorPrefs.locName.get())
    locPresenter.getLocationsDb()
  }

  private fun setUpToolbar() {
    with(searchToolbar) {
      setNavBtnListener { (fetchActivity() as MainActivity).goBack() }
      setOptionalBtnListener { checkPermissions() }
      if(!initialSetup) setSkipEventCount(2)
      observeSearchField()
          .subscribe(this@LocationFragment::checkQuery, Timber::e)
          .addTo(disposablesStorage1)
    }
  }

  private fun setUpList() {
    with(rvLocations) {
      layoutManager = LinearLayoutManager(fetchActivity())
      adapter = locAdapter
    }
  }

  private fun setUpMessage() {
    btnWarnReload.setOnClickListener { locPresenter.getLocationsRemote(searchToolbar.getQuery()) }
  }

  private fun checkQuery(query: String) {
    if(query.isNotEmpty()) {
      locPresenter.getLocationsRemote(query)
    } else {
      showPlaceHolder()
    }
  }

  @SuppressLint("CheckResult")
  private fun checkPermissions() {
    RxPermissions(fetchActivity() as FragmentActivity)
        .request(Manifest.permission.ACCESS_COARSE_LOCATION)
        .subscribe(this::onPermissionCheckResult, Timber::e)
  }

  @SuppressLint("MissingPermission")
  private fun getCurrentLocation() {
    networkManager
        .hasInternetConnection()
        .subscribe(this::onConnectionCheckResult, Timber::e)
        .addTo(disposablesStorage1)
  }

  private fun onPermissionCheckResult(hasPermission: Boolean) {
    if(hasPermission) getCurrentLocation() else showToast(getString(R.string.permission_error))
  }

  @SuppressLint("MissingPermission")
  private fun onConnectionCheckResult(hasConnection: Boolean) {
    if (hasConnection) {
      locManager.requestSingleUpdate(criteria, this, null)
    } else {
      showToast(getString(R.string.network_error))
    }
  }

  private fun onLocationApplied() {
    with(locAdapter.getData()) {
      if(second != -1) {
        locPresenter.saveLocations(first, second)
        proceedNext()
      } else {
        showToast(getString(R.string.locations_toast))
      }
    }
  }

  private fun proceedNext() {
    if(initialSetup) {
      (fetchActivity() as MainActivity).showWeather()
    } else {
      fetchActivity().onBackPressed()
    }
  }

  companion object {
    private const val INITIAL_SETUP = "initial setup"

    fun create(initialSetup: Boolean) = LocationFragment().apply {
      arguments = Bundle().apply {
        putBoolean(INITIAL_SETUP, initialSetup)
      }
    }
  }
}
