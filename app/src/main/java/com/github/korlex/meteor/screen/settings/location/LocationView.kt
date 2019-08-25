package com.github.korlex.meteor.screen.settings.location

import com.github.korlex.meteor.screen.settings.location.model.LocItem

interface LocationView {
    fun showLocationsRemote(locItems: List<LocItem>)
    fun showLocationsDb(locItems: List<LocItem>)
    fun showProgress()
    fun showEmpty()
    fun showError()
}