package com.github.korlex.meteor.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import com.github.korlex.meteor.R
import com.github.korlex.meteor.R.layout
import com.github.korlex.meteor.R.styleable
import kotlinx.android.synthetic.main.view_toolbar.view.btnNav
import kotlinx.android.synthetic.main.view_toolbar.view.btnOptional1
import kotlinx.android.synthetic.main.view_toolbar.view.btnOptional2
import kotlinx.android.synthetic.main.view_toolbar.view.tvTitle

class Toolbar(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

  init {
    View.inflate(context, layout.view_toolbar, this)
    val attributes = context.obtainStyledAttributes(attrs, styleable.Toolbar)
    val navBtnId = attributes.getResourceId(styleable.Toolbar_navBtn, -1)
    val optionalBtn1Id = attributes.getResourceId(styleable.Toolbar_optionalBtn1, -1)
    val optionalBtn2Id = attributes.getResourceId(styleable.Toolbar_optionalBtn2, -1)
    val titleText = attributes.getString(styleable.Toolbar_titleText)
    setNavBtn(navBtnId)
    setOptionalBtn1(optionalBtn1Id)
    setOptionalBtn2(optionalBtn2Id)
    setTitle(titleText)
    attributes.recycle()
  }

  fun setNavBtn(drawableId: Int?) {
    if(drawableId != null && drawableId != -1) {
      btnNav.setImageResource(drawableId)
      btnNav.visibility = View.VISIBLE
    }
  }

  fun setOptionalBtn1(drawableId: Int?) {
    if(drawableId != null && drawableId != -1) {
      btnOptional1.setImageResource(drawableId)
      btnOptional1.visibility = View.VISIBLE
    }
  }

  fun setOptionalBtn2(drawableId: Int?) {
    if(drawableId != null && drawableId != -1) {
      btnOptional2.setImageResource(drawableId)
      btnOptional2.visibility = View.VISIBLE
    }
  }

  fun setNavBtnListener(navBtnCallback: (() -> Unit)?) {
    if(navBtnCallback != null) {
      btnNav.setOnClickListener { navBtnCallback.invoke() }
    }
  }

  fun setOptionalBtn1Listener(optionalBtn1Callback: (() -> Unit)?) {
    if(optionalBtn1Callback != null) {
      btnOptional1.setOnClickListener { optionalBtn1Callback.invoke() }
    }
  }

  fun setOptionalBtn2Listener(optionalBtn2Callback: (() -> Unit)?) {
    if(optionalBtn2Callback != null) {
      btnOptional2.setOnClickListener { optionalBtn2Callback.invoke() }
    }
  }

  fun setTitle(textTitle: String?) {
    if(textTitle != null && !textTitle.isEmpty()) {
      tvTitle.text = textTitle
    }
  }
}