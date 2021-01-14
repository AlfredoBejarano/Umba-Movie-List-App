package me.alfredobejarano.movieslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.AndroidInjection
import me.alfredobejarano.movieslist.utils.SearchUiHandlerOwner

class NavHostActivity : AppCompatActivity(), SearchUiHandlerOwner {
    override var isSearchUiShowing = false
    private var navController: NavController? = null
    private var searchUiHandler: SearchUiHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_nav_host)
        navController = findNavController(R.id.navHostFragment)
    }

    /**
     * Detects if the search UI is being shown, if so, proceeds to close it. If not,
     * fallbacks to the normal back press behaviour.
     */
    override fun onBackPressed() = if (isSearchUiShowing) {
        searchUiHandler?.onBackPress() ?: Unit
    } else {
        super.onBackPressed()
    }

    /**
     * Sets the listener for the search view states.
     */
    override fun setListener(listener: SearchUiHandler?) {
        searchUiHandler = listener
    }

    /**
     * Proceeds to hide the search view when the user selects a Movie result.
     */
    override fun onSearchUiNavigation() {
        isSearchUiShowing = false
        searchUiHandler?.onBackPress()
    }

    /**
     * Tells to the view that report movie query text that the Movie results view is ready
     * to accept query values.
     */
    override fun onSearchUiReady() = searchUiHandler?.onSearchUiReady() ?: Unit

    /**
     * Interface that provides events related to the search view UI states.
     */
    interface SearchUiHandler {
        /**
         * Triggered when a [SearchUiHandlerOwner] performs some kind of up navigation.
         */
        fun onBackPress()

        /**
         * Triggered when the [SearchUiHandlerOwner] detects that the search view is ready to
         * accept queries.
         */
        fun onSearchUiReady()
    }
}
