package com.github.korlex.meteor.screen.settings.about


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.korlex.meteor.BaseFragment

import com.github.korlex.meteor.R
import kotlinx.android.synthetic.main.fragment_weather.toolbar

class AboutFragment : BaseFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_about, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    toolbar.setNavBtnListener { fetchActivity().onBackPressed() }
  }
}
