package com.github.korlex.meteor.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

inline fun <reified T : View> ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): T {
  return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot) as T
}

fun View.fadeIn(startAnimation: Boolean = true): Animator {
  val animator = ObjectAnimator.ofFloat(this, View.ALPHA, 1f)
  animator.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationStart(animation: Animator?) {
      visibility = View.VISIBLE
    }
  })
  animator.setAutoCancel(true)
  if (startAnimation) {
    animator.start()
  }
  return animator
}

fun View.fadeOut(hiddenVisibility: Int = View.GONE, startAnimation: Boolean = true): Animator? {
  if (visibility != View.VISIBLE) return null

  val animator = ObjectAnimator.ofFloat(this, View.ALPHA, 0f)
  animator.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      visibility = hiddenVisibility
    }
  })
  animator.setAutoCancel(true)
  if (startAnimation) {
    animator.start()
  }
  return animator
}