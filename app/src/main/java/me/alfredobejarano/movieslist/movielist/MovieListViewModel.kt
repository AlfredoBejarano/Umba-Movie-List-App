package me.alfredobejarano.movieslist.movielist

import androidx.lifecycle.ViewModel
import me.alfredobejarano.movieslist.utils.resultLiveData
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.domain.GetMovieListUseCase
import javax.inject.Inject

/**
 * Created by alfredo on 2019-08-02.
 */
class MovieListViewModel @Inject constructor(private val getListUseCase: GetMovieListUseCase) :
    ViewModel() {
    fun getMovieList(type: MovieListType) = resultLiveData {
        getListUseCase.getMovieList(type)
    }
}