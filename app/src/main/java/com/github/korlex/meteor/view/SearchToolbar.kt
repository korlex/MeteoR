package com.github.korlex.meteor.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import com.github.korlex.meteor.R
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_toolbar_search.view.btnClear
import kotlinx.android.synthetic.main.view_toolbar_search.view.btnSearch
import kotlinx.android.synthetic.main.view_toolbar_search.view.etSearchField
import java.util.concurrent.TimeUnit.MILLISECONDS

class SearchToolbar(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {

  private var clearBtnCallback: (() -> Unit)? = null

  init {
    View.inflate(context, R.layout.view_toolbar_search, this)
    val attributes = context.obtainStyledAttributes(attrs, R.styleable.SearchToolbar)
    val hintText = attributes.getString(R.styleable.SearchToolbar_hintText)
    setHintText(hintText)
    setClearBtnListener()
    attributes.recycle()
  }

  fun observeSearchField(): Observable<String> =
    etSearchField
        .afterTextChangeEvents()
        .map { it.editable().toString() }
        .doOnNext { setUpClearButton(it) }
        .skip(1)
        .debounce(900, MILLISECONDS, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())

  fun getQuery(): String = etSearchField.text.toString()

  fun removeFocus() {
    etSearchField.clearFocus()
  }

  private fun setHintText(text: String) {
    etSearchField.hint = text
  }

  private fun setUpClearButton(str: String) = with(btnClear) {
    visibility = if (!str.isEmpty()) VISIBLE else GONE
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