package me.alfredobejarano.movieslist.movielist

import me.alfredobejarano.movieslist.base.BasePresenter
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.domain.GetMovieListUseCase
import me.alfredobejarano.movieslist.utils.resultLiveData
import javax.inject.Inject

class MovieListPresenter @Inject constructor(private val getListUseCase: GetMovieListUseCase) :
    BasePresenter() {

    /**
     * Retrieves a List of Movies filtered by the given type.
     *
     * @param type - The type of movies to fetch
     *
     * @see MovieListType.MOVIE_LIST_POPULAR
     * @see MovieListType.MOVIE_LIST_UPCOMING
     * @see MovieListType.MOVIE_LIST_TOP_RATED
     */
    fun getMovieList(type: MovieListType) = resultLiveData {
        getListUseCase.getMovieList(type)
    }

}