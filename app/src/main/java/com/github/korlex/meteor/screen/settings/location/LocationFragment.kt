package com.github.korlex.meteor.screen.settings.location


import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.korlex.meteor.BaseFragment
import com.github.korlex.meteor.MainActivity

import com.github.korlex.meteor.R
import com.github.korlex.meteor.extensions.fadeIn
import com.github.korlex.meteor.extensions.fadeOut
import com.github.korlex.meteor.screen.settings.location.model.LocItem
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_location.btnApply
import kotlinx.android.synthetic.main.fragment_location.layoutMessage
import kotlinx.android.synthetic.main.fragment_location.layoutPlaceholder
import kotlinx.android.synthetic.main.fragment_location.pbLoading
import kotlinx.android.synthetic.main.fragment_location.rvLocations
import kotlinx.android.synthetic.main.fragment_location.searchToolbar
import kotlinx.android.synthetic.main.layout_message.btnMsgReload
import kotlinx.android.synthetic.main.layout_message.tvMsgTitle
import timber.log.Timber
import javax.inject.Inject

class LocationFragment : BaseFragment(), LocationView {

  @Inject lateinit var locPresenter: LocationPresenter
  private val locAdapter: LocationsAdapter = LocationsAdapter()
  private var initialSetup: Boolean = false

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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpToolbar()
    setUpList()
    setUpProgress()
    setUpMessage()
    btnApply.setOnClickListener { onLocationApplied() }
  }

  override fun showLocations(locItems: List<LocItem>) {
    locAdapter.locItems = locItems
    pbLoading.fadeOut()
    layoutMessage.fadeOut()
    layoutPlaceholder.fadeOut()
    rvLocations.fadeIn()
    btnApply.fadeIn()
  }

  override fun showProgress() {
    layoutMessage.fadeOut()
    layoutPlaceholder.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    pbLoading.fadeIn()
  }

  override fun showEmpty() {
    tvMsgTitle.text = getString(R.string.locations_empty)
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutPlaceholder.fadeOut()
    layoutMessage.fadeIn()
  }

  override fun showError() {
    tvMsgTitle.text = getString(R.string.locations_error)
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutPlaceholder.fadeOut()
    layoutMessage.fadeIn()
  }

  private fun showPlaceHolder() {
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutMessage.fadeOut()
    layoutPlaceholder.fadeIn()
  }

  private fun showToast() {
    Toast.makeText(fetchActivity(), R.string.locations_toast, Toast.LENGTH_SHORT).show()
  }

  private fun setUpToolbar() {
    searchToolbar
        .observeSearchField()
        .subscribe(this::checkQuery, Timber::e)
        .addTo(disposables)
  }

  private fun setUpList() {
    with(rvLocations) {
      layoutManager = LinearLayoutManager(fetchActivity())
      adapter = locAdapter
      addItemDecoration(DividerItemDecoration(fetchActivity(), LinearLayout.VERTICAL))
    }
  }

  private fun setUpProgress() {
    pbLoading.indeterminateDrawable.setColorFilter(
        ContextCompat.getColor(fetchActivity(), R.color.colorPrimaryDark),
        PorterDuff.Mode.SRC_IN)
  }

  private fun setUpMessage() {
    btnMsgReload.setOnClickListener { locPresenter.getResultByQuery(searchToolbar.getQuery()) }
  }

  private fun checkQuery(query: String) {
    if(!query.isEmpty()) locPresenter.getResultByQuery(query) else showPlaceHolder()
  }

  private fun onLocationApplied() {
    with(locAdapter.selectedItem) {
      if(this != null) {
        val loc = getString(R.string.loc_title, city, country)
        val lat = latitude.toString()
        val lon = longitude.toString()
        locPresenter.saveLocation(loc, lat, lon)
        proceedNext()
      } else {
        showToast()
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
