package com.github.korlex.meteor.screen.settings.location


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_location.layoutMsg
import kotlinx.android.synthetic.main.fragment_location.layoutPlaceholder
import kotlinx.android.synthetic.main.fragment_location.pbLoading
import kotlinx.android.synthetic.main.fragment_location.rvLocations
import kotlinx.android.synthetic.main.fragment_location.searchToolbar
import kotlinx.android.synthetic.main.layout_msg.btnMsgReload
import kotlinx.android.synthetic.main.layout_msg.tvMsgText
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
    setUpMessage()
    btnApply.setOnClickListener { onLocationApplied() }
  }

  override fun showLocations(locItems: List<LocItem>) {
    locAdapter.locItems = locItems
    pbLoading.fadeOut()
    layoutMsg.fadeOut()
    layoutPlaceholder.fadeOut()
    rvLocations.fadeIn()
    btnApply.fadeIn()
  }

  override fun showProgress() {
    layoutMsg.fadeOut()
    layoutPlaceholder.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    pbLoading.fadeIn()
  }

  override fun showEmpty() {
    tvMsgText.text = getString(R.string.locations_empty)
    btnMsgReload.visibility = View.GONE
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutPlaceholder.fadeOut()
    layoutMsg.fadeIn()
  }

  override fun showError() {
    tvMsgText.text = getString(R.string.locations_error)
    btnMsgReload.visibility = View.VISIBLE
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutPlaceholder.fadeOut()
    layoutMsg.fadeIn()
  }

  private fun showPlaceHolder() {
    pbLoading.fadeOut()
    rvLocations.fadeOut()
    btnApply.fadeOut()
    layoutMsg.fadeOut()
    layoutPlaceholder.fadeIn()
  }

  private fun showToast() {
    Toast.makeText(fetchActivity(), R.string.locations_toast, Toast.LENGTH_SHORT).show()
  }

  private fun setUpToolbar() {
    searchToolbar
        .observeSearchField()
        .subscribe(this::checkQuery, Timber::e)
        .addTo(disposablesStorage1)
  }

  private fun setUpList() {
    with(rvLocations) {
      layoutManager = LinearLayoutManager(fetchActivity())
      adapter = locAdapter
    }
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
        locPresenter.saveLocation(placeId, placeName)
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
