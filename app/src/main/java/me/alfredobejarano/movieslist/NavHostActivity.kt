package me.alfredobejarano.movieslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.AndroidInjection
import me.alfredobejarano.movieslist.di.ViewModelFactory
import javax.inject.Inject

class NavHostActivity : AppCompatActivity() {
    private var navController: NavController? = null

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var navHostViewModel: NavHostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_nav_host)
        navHostViewModel = ViewModelProvider(this, factory)[NavHostViewModel::class.java]
        navController = findNavController(R.id.navHostFragment)
    }

    override fun onBackPressed() = if (navHostViewModel.searchViewIsOpen) {
        navHostViewModel.closeSearchView()
    } else {
        super.onBackPressed()
    }
}
