package me.alfredobejarano.movieslist.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.alfredobejarano.movieslist.R
import me.alfredobejarano.movieslist.base.BaseFragment
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.movielist.MovieListFragment
import me.alfredobejarano.movieslist.utils.SearchUiHandlerOwner
import me.alfredobejarano.movieslist.utils.observeWith
import me.alfredobejarano.movieslist.utils.openMovieDetails

/**
 * Fragment class that will show to the user the result of a given movie query.
 */
class MovieSearchFragment : BaseFragment<MovieSearchPresenter>(),
    MovieListFragment.OnQueryChangeListener {
    companion object {
        const val FRAGMENT_TAG = "search_results"
    }

    private lateinit var searchListRecyclerView: RecyclerView

    /**
     * Creates the [RecyclerView] that will show the movie search results.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecyclerView(requireContext()).apply {
        id = R.id.searchResultsList
        layoutManager = LinearLayoutManager(context)
        searchListRecyclerView = this
        (requireActivity() as? SearchUiHandlerOwner)?.onSearchUiReady()
    }

    /**
     * Searches for a Movies in which their titles matches the query.
     * @param query Text to match against any movies title.
     */
    private fun searchMovie(query: String) = presenter.searchMovieByTitle(query).observeWith(
        owner = viewLifecycleOwner,
        onSuccess = ::renderMovieSearchResult,
        onError = { showError(this) { onQueryChanged(query) } }
    )

    /**
     * Displays the found Movies into the [searchListRecyclerView][RecyclerView widget].
     * @param list The results that came from the movie search.
     */
    private fun renderMovieSearchResult(list: List<Movie>) =
        searchListRecyclerView.adapter?.let { adapter ->
            (adapter as MovieSearchResultsListAdapter).updateList(list)
        } ?: run {
            searchListRecyclerView.adapter =
                MovieSearchResultsListAdapter(list) { movieId, view ->
                    (requireActivity() as? SearchUiHandlerOwner)?.onSearchUiNavigation()
                    openMovieDetails(movieId, view)
                }
        }

    /**
     * Triggered when a search bar provides a query text, proceeds to search for movies using the
     * query text to find potential matches form the movies title.
     *
     * @param query The text to use for matching.
     *
     * @see searchMovie
     */
    override fun onQueryChanged(query: String) {
        searchMovie(query)
    }
}