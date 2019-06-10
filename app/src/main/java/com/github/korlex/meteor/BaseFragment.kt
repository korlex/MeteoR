package com.github.korlex.meteor

import android.app.Activity
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {

  private val presenter: BasePresenter? by lazy { attachedPresenter() }
  val disposablesStorage1 = CompositeDisposable()
  val disposablesStorage2 = CompositeDisposable()

  override fun onDestroyView() {
    presenter?.onDetach()
    disposablesStorage1.clear()
    super.onDestroyView()
  }

  override fun onDestroy() {
    disposablesStorage2.clear()
    super.onDestroy()
  }

  open fun fetchActivity(): Activity = activity ?: throw NullPointerException("Activity is null")

  open fun attachedPresenter(): BasePresenter? = null
}