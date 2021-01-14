package me.alfredobejarano.movieslist.utils

import me.alfredobejarano.movieslist.NavHostActivity

/**
 * Interface that provides ease-of-use access to a class that has to manage a View that
 * searches for movies/
 */
interface SearchUiHandlerOwner {
    /**
     * Property that defines if the search UI is currently being shown to the user.
     */
    var isSearchUiShowing: Boolean

    /**
     * Sets the object that is listening for the search view UI states.
     */
    fun setListener(listener: NavHostActivity.SearchUiHandler?)

    /**
     * Triggered when the search UI is ready to be shown to the user.
     */
    fun onSearchUiReady()

    /**
     * Triggered when the user has chosen a Movie result from a query.
     */
    fun onSearchUiNavigation()
}