package com.github.korlex.meteor

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter {

    val disposables = CompositeDisposable()

    fun onDetach() { disposables.clear() }

}