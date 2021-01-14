package me.alfredobejarano.movieslist.search

import me.alfredobejarano.movieslist.base.BasePresenter
import me.alfredobejarano.movieslist.domain.SearchMovieByTitleUseCase
import me.alfredobejarano.movieslist.utils.resultLiveData
import javax.inject.Inject

class MovieSearchPresenter @Inject constructor(
    private val searchMovieByTitleUseCase: SearchMovieByTitleUseCase
): BasePresenter {
    fun searchMovieByTitle(query: String) = resultLiveData() {
        searchMovieByTitleUseCase.searchMovieByTitle(query)
    }
}