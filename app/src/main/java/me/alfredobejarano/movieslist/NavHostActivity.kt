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

    override fun onBackPressed() = if (isSearchUiShowing) {
        searchUiHandler?.onBackPress() ?: Unit
    } else {
        super.onBackPressed()
    }

    override fun setListener(listener: SearchUiHandler?) {
        searchUiHandler = listener
    }

    override fun onSearchUiNavigation() {
        isSearchUiShowing = false
        searchUiHandler?.onBackPress()
    }
    override fun onSearchUiReady() = searchUiHandler?.onSearchUiReady() ?: Unit

    interface SearchUiHandler {
        fun onBackPress()
        fun onSearchUiReady()
    }
}
