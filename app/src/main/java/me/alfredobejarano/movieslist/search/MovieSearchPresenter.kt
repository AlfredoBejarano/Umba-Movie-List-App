package me.alfredobejarano.movieslist.search

import me.alfredobejarano.movieslist.base.BasePresenter
import me.alfredobejarano.movieslist.domain.SearchMovieByTitleUseCase
import me.alfredobejarano.movieslist.utils.resultLiveData
import javax.inject.Inject

class MovieSearchPresenter @Inject constructor(
    private val searchMovieByTitleUseCase: SearchMovieByTitleUseCase
) : BasePresenter() {

    /**
     * Searches movies that their titles matches the given query text.
     *
     * @param query The text to use as the query.
     */
    fun searchMovieByTitle(query: String) = resultLiveData {
        searchMovieByTitleUseCase.searchMovieByTitle(query)
    }
}