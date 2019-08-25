package com.github.korlex.meteor.extensions

import android.content.Context
import android.util.TypedValue

fun Context.getThemeColorResId(resId: Int, resolveRefs: Boolean = true): Int {
  val typedValue = TypedValue()
  theme.resolveAttribute(resId, typedValue, resolveRefs)
  return typedValue.resourceId
}