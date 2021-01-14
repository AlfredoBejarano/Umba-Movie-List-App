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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.alfredobejarano.movieslist.NavHostActivity
import me.alfredobejarano.movieslist.R
import me.alfredobejarano.movieslist.R.anim.slide_in_left
import me.alfredobejarano.movieslist.R.anim.slide_in_right
import me.alfredobejarano.movieslist.R.id.searchResultsFrameLayout
import me.alfredobejarano.movieslist.base.BaseFragment
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.core.MovieListType.MOVIE_LIST_POPULAR
import me.alfredobejarano.movieslist.databinding.FragmentMovieListBinding
import me.alfredobejarano.movieslist.search.MovieSearchFragment
import me.alfredobejarano.movieslist.search.MovieSearchFragment.Companion.FRAGMENT_TAG
import me.alfredobejarano.movieslist.utils.SearchUiHandlerOwner
import me.alfredobejarano.movieslist.utils.hideSoftKeyboard
import me.alfredobejarano.movieslist.utils.isLoading
import me.alfredobejarano.movieslist.utils.observeWith
import me.alfredobejarano.movieslist.utils.openMovieDetails
import me.alfredobejarano.movieslist.utils.viewBinding

/**
 * Fragment class that displays lists of movies separated by categories.
 *
 * Created by alfredo on 2019-08-02.
 */
class MovieListFragment : BaseFragment<MovieListPresenter>(), NavHostActivity.SearchUiHandler {

    private var movieQuery: String = ""
    private var searchFragment: MovieSearchFragment? = null
    private val binding by viewBinding(FragmentMovieListBinding::inflate)

    private companion object {
        const val SEARCH_SCRIM_ALPHA = (256 * 0.8).toInt()
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(binding.popularMovieList)
        setupRecyclerView(binding.upcomingMovieList)
        setupRecyclerView(binding.topRatedMovieList)

        binding.searchResultsFrameLayout.background.alpha = SEARCH_SCRIM_ALPHA

        animateView(binding.searchBar, R.anim.slide_in_up) {
            setupSearchEditText(binding.searchBar)
        }

        fetchMovieList(MOVIE_LIST_POPULAR)
        fetchMovieList(MovieListType.MOVIE_LIST_UPCOMING)
        fetchMovieList(MovieListType.MOVIE_LIST_TOP_RATED)

        (requireActivity() as? SearchUiHandlerOwner)?.setListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideSearchResultFragment()
    }

    override fun onDestroy() {
        (requireActivity() as? SearchUiHandlerOwner)?.setListener(null)
        super.onDestroy()
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
        presenter.getMovieList(listType).observeWith(viewLifecycleOwner, binding::isLoading, {
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
        searchFragment = null
        val fragment = MovieSearchFragment()
        binding.searchResultsFrameLayout.visibility = View.VISIBLE
        parentFragmentManager.beginTransaction().replace(
            searchResultsFrameLayout, fragment,
            FRAGMENT_TAG
        ).commit()
        searchFragment = fragment
        (requireActivity() as? SearchUiHandlerOwner)?.isSearchUiShowing = true
        movieQuery = query
    }

    private fun hideSearchResultFragment() =
        searchFragment?.run {
            parentFragmentManager.beginTransaction().remove(this).commit()
            binding.searchResultsFrameLayout.visibility = View.GONE
            hideSoftKeyboard()
            binding.searchBar.text.clear()
            searchFragment = null
            (requireActivity() as? SearchUiHandlerOwner)?.isSearchUiShowing = false
        }

    override fun onSearchUiReady() = searchFragment?.onQueryChanged(movieQuery) ?: Unit

    override fun onBackPress() = hideSearchResultFragment() ?: Unit

    interface OnQueryChangeListener {
        fun onQueryChanged(query: String)
    }
}