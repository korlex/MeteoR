package com.github.korlex.meteor.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun View.fadeIn(startAnimation: Boolean = true) {
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
}

fun View.fadeOut(hiddenVisibility: Int = View.GONE, startAnimation: Boolean = true) {
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
}