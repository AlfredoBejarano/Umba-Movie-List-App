package me.alfredobejarano.movieslist.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.AnimRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.movieslist.NavHostViewModel
import me.alfredobejarano.movieslist.R
import me.alfredobejarano.movieslist.R.anim.slide_in_left
import me.alfredobejarano.movieslist.R.anim.slide_in_right
import me.alfredobejarano.movieslist.R.id.searchResultsFrameLayout
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.core.MovieListType.MOVIE_LIST_POPULAR
import me.alfredobejarano.movieslist.databinding.FragmentMovieListBinding
import me.alfredobejarano.movieslist.di.ViewModelFactory
import me.alfredobejarano.movieslist.search.MovieSearchFragment
import me.alfredobejarano.movieslist.search.MovieSearchFragment.Companion.FRAGMENT_TAG
import me.alfredobejarano.movieslist.utils.hideSoftKeyboard
import me.alfredobejarano.movieslist.utils.isLoading
import me.alfredobejarano.movieslist.utils.observeWith
import me.alfredobejarano.movieslist.utils.openMovieDetails
import me.alfredobejarano.movieslist.utils.viewBinding
import javax.inject.Inject

/**
 * Fragment class that displays lists of movies separated by categories.
 *
 * Created by alfredo on 2019-08-02.
 */
class MovieListFragment : Fragment() {
    private companion object {
        const val SEARCH_SCRIM_ALPHA = (256 * 0.8).toInt()
    }

    @Inject
    lateinit var factory: ViewModelFactory
    private val binding by viewBinding(FragmentMovieListBinding::inflate)

    private lateinit var viewModel: MovieListViewModel
    private lateinit var navHostViewModel: NavHostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this@MovieListFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(binding.popularMovieList)
        setupRecyclerView(binding.upcomingMovieList)
        setupRecyclerView(binding.topRatedMovieList)

        viewModel = ViewModelProvider(this, factory)[MovieListViewModel::class.java]
        navHostViewModel = ViewModelProvider(requireActivity())[NavHostViewModel::class.java]

        navHostViewModel.closeSearchViewLiveData.observe(viewLifecycleOwner, {
            hideSearchResultFragment()
        })

        binding.searchResultsFrameLayout.background.alpha = SEARCH_SCRIM_ALPHA

        animateView(binding.searchBar, R.anim.slide_in_up) {
            setupSearchEditText(binding.searchBar)
        }

        fetchMovieList(MOVIE_LIST_POPULAR)
        fetchMovieList(MovieListType.MOVIE_LIST_UPCOMING)
        fetchMovieList(MovieListType.MOVIE_LIST_TOP_RATED)
    }

    private fun setupSearchEditText(editText: EditText) = editText.run {
        addTextChangedListener { query ->
            if (query?.length ?: 0 > 0) {
                displaySearchResultFragment(query?.toString() ?: "")
            } else {
                hideSearchResultFragment()
            }
        }
        setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard()
                handled = true
            }
            handled
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) = recyclerView.apply {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun fetchMovieList(listType: MovieListType) =
        viewModel.getMovieList(listType).observeWith(viewLifecycleOwner, binding::isLoading, {
            renderMovieList(this, listType)
        }, {})

    private fun movieListRecyclerViewFactory(type: MovieListType) = when (type) {
        MOVIE_LIST_POPULAR -> binding.popularMovieList
        MovieListType.MOVIE_LIST_UPCOMING -> binding.upcomingMovieList
        MovieListType.MOVIE_LIST_TOP_RATED -> binding.topRatedMovieList
    }

    private fun renderMovieList(list: List<Movie>, type: MovieListType) {
        if (list.isEmpty()) {
            Log.d(this.javaClass.name, "Empty List")
        } else {
            movieListRecyclerViewFactory(type).run {
                updateMovieAdapter(list)
                animateView(this, if (type == MOVIE_LIST_POPULAR) slide_in_right else slide_in_left)
            }
        }
    }

    private fun RecyclerView.updateMovieAdapter(list: List<Movie>) = adapter?.let { adapter ->
        (adapter as? MovieListAdapter)?.updateList(list) ?: createMovieAdapter(list)
    } ?: run {
        createMovieAdapter(list)
    }

    private fun RecyclerView.createMovieAdapter(list: List<Movie>) {
        adapter = MovieListAdapter(list, ::openMovieDetails)
    }

    private fun animateView(view: View, @AnimRes resId: Int, onAnimationEnd: () -> Unit = {}) {
        val animation = AnimationUtils.loadAnimation(requireContext(), resId)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(anim: Animation?) = Unit
            override fun onAnimationRepeat(anim: Animation?) = Unit
            override fun onAnimationEnd(anim: Animation?) = onAnimationEnd()
        })
        view.startAnimation(animation)
    }

    private fun displaySearchResultFragment(query: String) {
        binding.searchResultsFrameLayout.visibility = View.VISIBLE
        navHostViewModel.reportQueryChange(query)
        parentFragmentManager.beginTransaction().replace(
            searchResultsFrameLayout, MovieSearchFragment(),
            FRAGMENT_TAG
        ).commit()
        navHostViewModel.searchViewIsOpen = true
    }

    private fun hideSearchResultFragment() =
        parentFragmentManager.findFragmentByTag(FRAGMENT_TAG)?.run {
            parentFragmentManager.beginTransaction().remove(this).commit()
            binding.searchResultsFrameLayout.visibility = View.GONE
            hideSoftKeyboard()
            binding.searchBar.text.clear()
            navHostViewModel.searchViewIsOpen = false
        }
}