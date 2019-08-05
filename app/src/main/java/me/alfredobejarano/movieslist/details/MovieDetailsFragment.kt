package me.alfredobejarano.movieslist.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.movieslist.core.Result
import me.alfredobejarano.movieslist.di.ViewModelFactory
import javax.inject.Inject

class MovieDetailsFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: MovieDetailsViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        TextView(requireContext()).also {
            AndroidSupportInjection.inject(this)
            viewModel = ViewModelProviders.of(this, factory)[MovieDetailsViewModel::class.java]
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovieDetails(arguments?.getInt("movieId") ?: 0)
    }

    private fun getMovieDetails(movieId: Int) = viewModel.getMovieDetails(movieId).observe(this, Observer { result ->
        when (result.status) {
            Result.Status.OK -> (requireView() as? TextView)?.text = result.payload?.toString() ?: "Not found!"
            Result.Status.ERROR -> Log.d(this.javaClass.name, result.error ?: "")
            Result.Status.LOADING -> Log.d(this.javaClass.name, "Loading")
        }
    })
}