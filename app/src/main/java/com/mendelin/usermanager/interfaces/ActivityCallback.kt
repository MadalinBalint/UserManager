package com.mendelin.usermanager.interfaces

interface ActivityCallback {
    fun showToolbar(isShown: Boolean)
    fun setToolbarTitle(title: String = "")
}