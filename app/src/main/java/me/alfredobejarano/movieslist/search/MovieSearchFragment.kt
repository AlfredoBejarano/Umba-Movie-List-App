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

class MovieSearchFragment : BaseFragment<MovieSearchPresenter>(),
    MovieListFragment.OnQueryChangeListener {
    companion object {
        const val FRAGMENT_TAG = "search_results"
    }

    private lateinit var searchListRecyclerView: RecyclerView

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

    private fun searchMovie(query: String) = presenter.searchMovieByTitle(query).observeWith(
        owner = viewLifecycleOwner,
        onSuccess = ::renderMovieSearchResult,
        onError = {}
    )

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

    override fun onQueryChanged(query: String) = searchMovie(query)
}