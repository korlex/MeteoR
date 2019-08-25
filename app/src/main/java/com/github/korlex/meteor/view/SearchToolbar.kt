package com.github.korlex.meteor.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import com.github.korlex.meteor.R
import com.github.korlex.meteor.R.styleable
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_toolbar_search.view.btnClear
import kotlinx.android.synthetic.main.view_toolbar_search.view.btnNav
import kotlinx.android.synthetic.main.view_toolbar_search.view.btnOptional
import kotlinx.android.synthetic.main.view_toolbar_search.view.etSearchField
import java.util.concurrent.TimeUnit.MILLISECONDS

class SearchToolbar(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

  private var clearBtnCallback: (() -> Unit)? = null
  private var filterQuery: String? = null
  private var skipEventCount: Long = 1L

  init {
    View.inflate(context, R.layout.view_toolbar_search, this)
    val attributes = context.obtainStyledAttributes(attrs, R.styleable.SearchToolbar)
    val navBtnId = attributes.getResourceId(styleable.SearchToolbar_searchBtn, -1)
    val hintText = attributes.getString(styleable.SearchToolbar_hintText)
    val optBtnId = attributes.getResourceId(styleable.SearchToolbar_optionalBtn, -1)
    filterQuery = attributes.getString(styleable.SearchToolbar_filterQuery)
    setNavBtn(navBtnId)
    setHintText(hintText)
    setOptionalBtn(optBtnId)
    setClearBtnListener()
    attributes.recycle()
  }

  fun observeSearchField(): Observable<String> =
    etSearchField
        .afterTextChangeEvents()
        .map { it.editable().toString() }
        .doOnNext { showClearBtn(it) }
        .filter { it != filterQuery }
        .skip(skipEventCount)
        .debounce(900, MILLISECONDS, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())


  fun setNavBtnListener(navBtnCallback: (() -> Unit)?) {
    if(navBtnCallback != null) {
      btnNav.setOnClickListener { navBtnCallback.invoke() }
    }
  }

  fun setOptionalBtnListener(optBtnCallback: (() -> Unit)?) {
    if(optBtnCallback != null) {
      btnOptional.setOnClickListener { optBtnCallback.invoke() }
    }
  }

  fun setQuery(query: String) { etSearchField.setText(query) }

  fun getQuery(): String = etSearchField.text.toString()

  fun removeFocus() {
    etSearchField.clearFocus()
  }

  fun setSkipEventCount(skipEventCount: Long) {
    this.skipEventCount = skipEventCount
  }

  private fun setNavBtn(drawableId: Int) {
    if(drawableId != -1) {
      btnNav.setImageResource(drawableId)
      btnNav.visibility = View.VISIBLE
    }
  }

  private fun setOptionalBtn(drawableId: Int) {
    if(drawableId != -1) {
      btnOptional.setImageResource(drawableId)
      btnOptional.visibility = View.VISIBLE
    }
  }

  private fun setHintText(text: String?) {
    etSearchField.hint = text
  }


  private fun showClearBtn(str: String) {
    btnClear.visibility = if(str.isNotEmpty()) View.VISIBLE else View.GONE
  }

  private fun setClearBtnListener() {
    btnClear.setOnClickListener {
      etSearchField.isEnabled = true
      etSearchField.text.clear()
      clearBtnCallback?.invoke()
      clearBtnCallback = null
    }
  }
}