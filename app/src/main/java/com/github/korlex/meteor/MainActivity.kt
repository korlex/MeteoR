package com.github.korlex.meteor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import com.github.korlex.meteor.extensions.fadeOut
import com.github.korlex.meteor.preferences.MeteorPrefs
import com.github.korlex.meteor.screen.settings.location.LocationFragment
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
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    AndroidInjection.inject(this)
    manageStartLaunch()
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

  private fun manageStartLaunch() {
    Handler().postDelayed(
        { if(meteorPrefs.isFilled()) showWeather() else replaceFragment(WelcomeFragment()) }, 1000
    )
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
