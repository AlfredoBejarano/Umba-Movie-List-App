package me.alfredobejarano.movieslist.movielist.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.core.Result
import me.alfredobejarano.movieslist.di.ViewModelFactory
import javax.inject.Inject

/**
 * Base class for a fragment that displays a list of movies.
 *
 * Implementations of this class will display a different list of movies
 * based on the [listType] abstract value.
 *
 * Created by alfredo on 2019-08-02.
 */
abstract class MovieListFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory
    abstract val listType: MovieListType
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MovieListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedState: Bundle?
    ) = RecyclerView(requireContext()).apply {
        AndroidSupportInjection.inject(this@MovieListFragment)
        layoutManager = GridLayoutManager(context, 2)
        viewModel =
            ViewModelProviders.of(this@MovieListFragment, factory)[MovieListViewModel::class.java]
    }.also {
        recyclerView = it
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchMovieList()
    }

    private fun fetchMovieList() =
        viewModel.getMovieList(listType).observe(this, Observer { result ->
            when (result.status) {
                Result.Status.LOADING -> Log.d(this.javaClass.name, "Loading")
                Result.Status.OK -> renderMovieList(result.payload ?: emptyList())
                Result.Status.ERROR -> Log.d(this.javaClass.name, result.error ?: "Error")
            }
        })

    private fun renderMovieList(list: List<Movie>) {
        if (list.isEmpty()) {
            Log.d(this.javaClass.name, "Empty List")
        } else {
            recyclerView.adapter?.let { adapter ->
                (adapter as? MovieListAdapter)?.updateList(list)
            } ?: run {
                recyclerView.adapter =
                    MovieListAdapter(list)
            }
        }
    }
}