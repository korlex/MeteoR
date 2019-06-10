package com.github.korlex.meteor.screen.settings.color


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.korlex.meteor.BaseFragment

import com.github.korlex.meteor.R
import com.github.korlex.meteor.preferences.MeteorPrefs
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_color.btnBlue
import kotlinx.android.synthetic.main.fragment_color.btnGreen
import kotlinx.android.synthetic.main.fragment_color.btnRed
import kotlinx.android.synthetic.main.fragment_color.tvBlueTitle
import kotlinx.android.synthetic.main.fragment_color.tvGreenTitle
import kotlinx.android.synthetic.main.fragment_color.tvRedTitle
import javax.inject.Inject

class ColorFragment : BaseFragment() {

  @Inject lateinit var meteoPrefs: MeteorPrefs
  private lateinit var selectedColor: String

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    AndroidSupportInjection.inject(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    selectedColor = meteoPrefs.colorScheme.get()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_color, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupBtnBlue()
    setupBtnGreen()
    setupBtnRed()
  }

  private fun setupBtnBlue() {
    with(btnBlue) {
      if(tag == selectedColor) tvBlueTitle.isSelected = true
      setOnClickListener { applyNewColorScheme(tag as String) }
    }
  }

  private fun setupBtnGreen() {
    with(btnGreen) {
      if(tag == selectedColor) tvGreenTitle.isSelected = true
      setOnClickListener { applyNewColorScheme(tag as String) }
    }
  }

  private fun setupBtnRed() {
    with(btnRed) {
      if(tag == selectedColor) tvRedTitle.isSelected = true
      setOnClickListener { applyNewColorScheme(tag as String) }
    }
  }

  private fun applyNewColorScheme(tag: String) {
    meteoPrefs.colorScheme.set(tag)
    fetchActivity().recreate()
  }
}
