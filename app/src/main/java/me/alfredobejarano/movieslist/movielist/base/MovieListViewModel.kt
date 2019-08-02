package me.alfredobejarano.movieslist.movielist.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.launchInIOForLiveData
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.domain.GetMovieListUseCase

/**
 * Created by alfredo on 2019-08-02.
 */
class MovieListViewModel(private val getListUseCase: GetMovieListUseCase) : ViewModel() {
    fun getMovieList(type: MovieListType) = launchInIOForLiveData {
        getListUseCase.getMovieList(type)
    }
}