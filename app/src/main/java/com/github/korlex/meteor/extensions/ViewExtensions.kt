package com.github.korlex.meteor.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import com.github.korlex.meteor.R

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

fun ImageView.setIcon(iconId: Int) {
  when(iconId) {
    in 200..232 -> setImageResource(R.drawable.ic_thunderstorm)
    in 300..321 -> setImageResource(R.drawable.ic_shower_rain)
    in 500..504 -> setImageResource(R.drawable.ic_rain)
    in 520..531 -> setImageResource(R.drawable.ic_rain)
    in 600..622 -> setImageResource(R.drawable.ic_snow)
    in 701..781 -> setImageResource(R.drawable.ic_mist)
    in 803..804 -> setImageResource(R.drawable.ic_broken_clouds)

    511 -> setImageResource(R.drawable.ic_snow)
    800 -> setImageResource(R.drawable.ic_clear_sky)
    801 -> setImageResource(R.drawable.ic_few_clouds)
    802 -> setImageResource(R.drawable.ic_few_clouds)
    else -> throw IllegalArgumentException("wrong iconId")
  }
}