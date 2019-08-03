package me.alfredobejarano.movieslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.AndroidInjection
import me.alfredobejarano.movieslist.di.Injector

class NavHostActivity : AppCompatActivity() {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Injector.init(application)

        setContentView(R.layout.activity_nav_host)
        navController = findNavController(R.id.navHostFragment)
    }
}
