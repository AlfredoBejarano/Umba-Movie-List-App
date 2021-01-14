package me.alfredobejarano.movieslist.movielist

import me.alfredobejarano.movieslist.base.BasePresenter
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.domain.GetMovieListUseCase
import me.alfredobejarano.movieslist.utils.resultLiveData
import javax.inject.Inject

class MovieListPresenter @Inject constructor(private val getListUseCase: GetMovieListUseCase) :
    BasePresenter {

    fun getMovieList(type: MovieListType) = resultLiveData {
        getListUseCase.getMovieList(type)
    }

}