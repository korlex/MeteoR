package com.github.korlex.meteor.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import com.github.korlex.meteor.R
import kotlinx.android.synthetic.main.view_toolbar.view.btnNav
import kotlinx.android.synthetic.main.view_toolbar.view.btnOptional
import kotlinx.android.synthetic.main.view_toolbar.view.tvTitle

class Toolbar(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

  init {
    View.inflate(context, R.layout.view_toolbar, this)
    val attributes = context.obtainStyledAttributes(attrs, R.styleable.Toolbar)
    val navBtnId = attributes.getResourceId(R.styleable.Toolbar_navBtn, -1)
    val optionalBtnId = attributes.getResourceId(R.styleable.Toolbar_optionalBtn, -1)
    val titleText = attributes.getString(R.styleable.Toolbar_titleText)
    setNavBtn(navBtnId)
    setOptionalBtn(optionalBtnId)
    setTitle(titleText)
    attributes.recycle()
  }

  fun setNavBtn(drawableId: Int?) {
    if(drawableId != null && drawableId != -1) {
      btnNav.setImageResource(drawableId)
      btnNav.visibility = View.VISIBLE
    }
  }

  fun setNavBtnListener(navBtnCallback: (() -> Unit)?) {
    if(navBtnCallback != null) {
      btnNav.setOnClickListener { navBtnCallback.invoke() }
    }
  }

  fun setOptionalBtn(drawableId: Int?) {
    if(drawableId != null && drawableId != -1) {
      btnOptional.setImageResource(drawableId)
      btnOptional.visibility = View.VISIBLE
    }
  }

  fun setOptionalBtnListener(optionalBtnCallback: (() -> Unit)?) {
    if(optionalBtnCallback != null) {
      btnOptional.setOnClickListener { optionalBtnCallback.invoke() }
    }
  }

  fun setTitle(textTitle: String?) {
    if(textTitle != null && !textTitle.isEmpty()) {
      tvTitle.text = textTitle
    }
  }
}