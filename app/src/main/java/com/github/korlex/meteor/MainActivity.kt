package com.github.korlex.meteor

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.github.korlex.meteor.preferences.MeteorPrefs
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.FOREST_GREEN
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.OCEAN_BLUE
import com.github.korlex.meteor.preferences.MeteorPrefs.Companion.SUNSET_RED
import com.github.korlex.meteor.screen.weather.WeatherFragment
import com.github.korlex.meteor.screen.welcome.WelcomeFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.layoutBrandLaunch
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
  @Inject
  lateinit var meteorPrefs: MeteorPrefs

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    initColorScheme()
    setContentView(R.layout.activity_main)
    manageStartLaunch(savedInstanceState)
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

  private fun initColorScheme() {
    when(meteorPrefs.colorScheme.get()) {
      OCEAN_BLUE   -> setTheme(R.style.AppTheme_OceanBlue)
      FOREST_GREEN -> setTheme(R.style.AppTheme_ForestGreen)
      SUNSET_RED   -> setTheme(R.style.AppTheme_SunsetRed)
    }
  }

  private fun manageStartLaunch(savedInstanceState: Bundle?) {
    if(savedInstanceState == null) {
      Handler().postDelayed(
          { if(meteorPrefs.locId.isSet) showWeather() else replaceFragment(WelcomeFragment()) }, 1000
      )
    } else {
      layoutBrandLaunch.visibility = View.GONE

    }
  }

  private fun closeKeyboard(): Boolean {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
  }


  fun goBack() {
    if(!closeKeyboard()) onBackPressed()
  }

  fun showWeather() {
    layoutBrandLaunch.visibility = View.GONE
    replaceFragment(WeatherFragment())
  }

  fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    val tag = fragment.javaClass.simpleName
    Timber.tag(this.javaClass.simpleName).i("replaceFragment() $tag")
    val transaction = supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
            R.animator.fragment_open_enter, R.animator.fragment_open_exit,
            R.animator.fragment_close_enter, R.animator.fragment_close_exit
        )
        .replace(R.id.fragmentContainer, fragment)
    if (addToBackStack) { transaction.addToBackStack(tag) }
    transaction.commitAllowingStateLoss()
  }

}
