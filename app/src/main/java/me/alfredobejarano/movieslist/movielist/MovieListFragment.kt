package me.alfredobejarano.movieslist.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.movieslist.R
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.core.Result
import me.alfredobejarano.movieslist.databinding.FragmentMovieListBinding
import me.alfredobejarano.movieslist.di.ViewModelFactory
import javax.inject.Inject

/**
 * Fragment class that displays lists of movies separated by categories.
 *
 * Created by alfredo on 2019-08-02.
 */
class MovieListFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: MovieListViewModel
    private lateinit var popularRecyclerView: RecyclerView
    private lateinit var topRatedRecyclerView: RecyclerView
    private lateinit var upcomingRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedState: Bundle?
    ) = FragmentMovieListBinding.inflate(inflater, container, false).apply {
        AndroidSupportInjection.inject(this@MovieListFragment)

        popularRecyclerView = setupRecyclerView(popularMovieList)
        upcomingRecyclerView = setupRecyclerView(upcomingMovieList)
        topRatedRecyclerView = setupRecyclerView(topRatedMovieList)

        viewModel = ViewModelProviders.of(this@MovieListFragment, factory)[MovieListViewModel::class.java]

        animateView(searchBar, R.anim.slide_in_up)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchMovieList(MovieListType.MOVIE_LIST_POPULAR)
        fetchMovieList(MovieListType.MOVIE_LIST_UPCOMING)
        fetchMovieList(MovieListType.MOVIE_LIST_TOP_RATED)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) = recyclerView.apply {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchMovieList(listType: MovieListType) =
        viewModel.getMovieList(listType).observe(this, Observer { result ->
            when (result.status) {
                Result.Status.LOADING -> Log.d(this.javaClass.name, "Loading")
                Result.Status.OK -> renderMovieList(result.payload ?: emptyList(), listType)
                Result.Status.ERROR -> Log.d(this.javaClass.name, result.error ?: "Error")
            }
        })

    private fun movieListRecyclerViewFactory(type: MovieListType) = when (type) {
        MovieListType.MOVIE_LIST_POPULAR -> popularRecyclerView
        MovieListType.MOVIE_LIST_UPCOMING -> upcomingRecyclerView
        MovieListType.MOVIE_LIST_TOP_RATED -> topRatedRecyclerView
    }

    private fun renderMovieList(list: List<Movie>, type: MovieListType) {
        if (list.isEmpty()) {
            Log.d(this.javaClass.name, "Empty List")
        } else {
            movieListRecyclerViewFactory(type).run {
                adapter?.let { adapter ->
                    (adapter as? MovieListAdapter)?.updateList(list)
                } ?: run {
                    adapter = MovieListAdapter(list)
                }
                animateView(
                    this, if (type == MovieListType.MOVIE_LIST_POPULAR) {
                        R.anim.slide_in_right
                    } else {
                        R.anim.slide_in_left
                    }
                )
            }
        }
    }


    private fun animateView(view: View, @AnimRes animationRes: Int, onAnimationEnd: () -> Unit = {}) {
        val animation = AnimationUtils.loadAnimation(requireContext(), animationRes)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) = Unit
            override fun onAnimationEnd(p0: Animation?) = onAnimationEnd()
            override fun onAnimationStart(p0: Animation?) {
                View.VISIBLE
            }
        })
        view.startAnimation(animation)
    }
}