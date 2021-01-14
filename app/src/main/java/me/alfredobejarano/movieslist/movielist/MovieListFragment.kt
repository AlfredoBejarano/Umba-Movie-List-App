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
 */
class MovieListFragment : BaseFragment<MovieListPresenter>(), NavHostActivity.SearchUiHandler {

    /**
     * The last user query
     */
    private var movieQuery: String = ""

    /**
     * Reference to the fragment responsible for Movie searches.
     */
    private var searchFragment: MovieSearchFragment? = null

    private val binding by viewBinding(FragmentMovieListBinding::inflate)

    private companion object {
        /**
         * Defines the opacity for the movie results background.
         */
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

    override fun onDestroy() {
        (requireActivity() as? SearchUiHandlerOwner)?.setListener(null)
        super.onDestroy()
    }

    /**
     * Adds a listener to the search bar that will trigger a movie search when the text gets changed.
     */
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

    /**
     * Receives a [RecyclerView] and assigns a [LinearLayoutManager] to it.
     * @param recyclerView The view to assing the layout manager to.
     */
    private fun setupRecyclerView(recyclerView: RecyclerView) = recyclerView.apply {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    /**
     * Retrieves a list of movies depending the requested type.
     * @param listType The type of movies to display.
     *
     * @see MovieListType.MOVIE_LIST_POPULAR
     * @see MovieListType.MOVIE_LIST_UPCOMING
     * @see MovieListType.MOVIE_LIST_TOP_RATED
     */
    private fun fetchMovieList(listType: MovieListType) =
        presenter.getMovieList(listType).observeWith(viewLifecycleOwner, binding::isLoading, {
            renderMovieList(this, listType)
        }, { showError(this) { fetchMovieRetry(listType) } })

    /**
     * Wrapper function to retry fetching a movie when an error happens.
     */
    private fun fetchMovieRetry(listType: MovieListType) {
        fetchMovieList(listType)
    }

    /**
     * Retrieves the corresponding [RecyclerView] view responsible for showing a given [MovieListType].
     * @param type The type movie to get the view from.
     *
     * @see MovieListType.MOVIE_LIST_POPULAR
     * @see MovieListType.MOVIE_LIST_UPCOMING
     * @see MovieListType.MOVIE_LIST_TOP_RATED
     */
    private fun movieListRecyclerViewFactory(type: MovieListType) = when (type) {
        MOVIE_LIST_POPULAR -> binding.popularMovieList
        MovieListType.MOVIE_LIST_UPCOMING -> binding.upcomingMovieList
        MovieListType.MOVIE_LIST_TOP_RATED -> binding.topRatedMovieList
    }

    /**
     * Draws a [List] of [Movie] objects into a [RecyclerView]. The [RecyclerView] gets chosen
     * by the given [movie type][MovieListType].
     *
     * @param list The [List] of [Movie] objects to display into the [RecyclerView].
     * @param type The type of Movies that are going to be displayed.
     *
     * @see MovieListType.MOVIE_LIST_POPULAR
     * @see MovieListType.MOVIE_LIST_UPCOMING
     * @see MovieListType.MOVIE_LIST_TOP_RATED
     */
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

    /**
     * Updates the current items at the given [RecyclerView] [adapter][MovieListAdapter].
     * If no adapter has been attached, proceeds to create one using the given [List] of [Movie] objects.
     *
     * @see createMovieAdapter
     */
    private fun RecyclerView.updateMovieAdapter(list: List<Movie>) = adapter?.let { adapter ->
        (adapter as? MovieListAdapter)?.updateList(list) ?: createMovieAdapter(list)
    } ?: run {
        createMovieAdapter(list)
    }

    /**
     * Creates a [MovieListAdapter] instance using the given [List] of [Movie] objects in order
     * to display them into a [RecyclerView].
     *
     * @param list The List of Movies to display.
     */
    private fun RecyclerView.createMovieAdapter(list: List<Movie>) {
        adapter = MovieListAdapter(list, ::openMovieDetails)
    }

    /**
     * Starts the given animation and applies it to a given view.
     *
     * @param view The view to animate
     * @param resId Resource Id of the animation to apply.
     * @param onAnimationEnd Triggered when the animated view finishes the applied animation.
     */
    private fun animateView(view: View, @AnimRes resId: Int, onAnimationEnd: () -> Unit = {}) {
        val animation = AnimationUtils.loadAnimation(requireContext(), resId)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(anim: Animation?) = Unit
            override fun onAnimationRepeat(anim: Animation?) = Unit
            override fun onAnimationEnd(anim: Animation?) = onAnimationEnd()
        })
        view.startAnimation(animation)
    }

    /**
     * Draws the List of movies thrown by a movie search.
     * @param query The query typed into the movie search bar.
     */
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

    /**
     * Dismisses the List of Movies that were shown by a Movie search.
     */
    private fun hideSearchResultFragment() =
        searchFragment?.run {
            parentFragmentManager.beginTransaction().remove(this).commit()
            binding.searchResultsFrameLayout.visibility = View.GONE
            hideSoftKeyboard()
            binding.searchBar.text.clear()
            searchFragment = null
            (requireActivity() as? SearchUiHandlerOwner)?.isSearchUiShowing = false
        }

    /**
     * Reports a query to the Fragment that searches for Movies when said fragment is ready
     * to accept queries.
     */
    override fun onSearchUiReady() = searchFragment?.onQueryChanged(movieQuery) ?: Unit

    /**
     * Triggered when the parent Activity pressed the back button. Proceeds to hide the
     * List of result form a Movie search.
     *
     * @see hideSearchResultFragment
     */
    override fun onBackPress() = hideSearchResultFragment() ?: Unit

    /**
     * Listener that will receive a query text when the search bar changes text.
     */
    interface OnQueryChangeListener {
        fun onQueryChanged(query: String)
    }
}