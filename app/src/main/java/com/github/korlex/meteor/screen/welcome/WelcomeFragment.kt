package com.github.korlex.meteor.screen.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.korlex.meteor.BaseFragment
import com.github.korlex.meteor.MainActivity

import com.github.korlex.meteor.R
import com.github.korlex.meteor.screen.settings.location.LocationFragment
import kotlinx.android.synthetic.main.fragment_welcome.btnSettings

class WelcomeFragment : BaseFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_welcome, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btnSettings.setOnClickListener {
      (activity as? MainActivity)?.replaceFragment(LocationFragment.create(true), true)
    }
  }
}
