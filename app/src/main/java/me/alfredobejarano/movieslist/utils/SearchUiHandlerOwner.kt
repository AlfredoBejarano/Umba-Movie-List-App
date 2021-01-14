package me.alfredobejarano.movieslist.utils

import me.alfredobejarano.movieslist.NavHostActivity

interface SearchUiHandlerOwner {
    var isSearchUiShowing: Boolean

    fun setListener(listener: NavHostActivity.SearchUiHandler?)
    fun onSearchUiReady()
    fun onSearchUiNavigation()
}