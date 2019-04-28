package com.github.korlex.meteor

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {

  private val presenter: BasePresenter? by lazy { attachedPresenter() }
  val disposables = CompositeDisposable()

  override fun onDestroyView() {
    presenter?.onDetach()
    disposables.clear()
    super.onDestroyView()
  }


  open fun fetchActivity(): Activity = activity ?: throw NullPointerException("Activity is null")

  open fun attachedPresenter(): BasePresenter? = null
}