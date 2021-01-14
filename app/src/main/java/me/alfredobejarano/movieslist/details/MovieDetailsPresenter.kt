package me.alfredobejarano.movieslist.details

import me.alfredobejarano.movieslist.base.BasePresenter
import me.alfredobejarano.movieslist.domain.GetMovieDetailsUseCase
import me.alfredobejarano.movieslist.utils.resultLiveData
import javax.inject.Inject

class MovieDetailsPresenter @Inject constructor(private val movieDetailsUseCase: GetMovieDetailsUseCase) :
    BasePresenter {

    /**
     * Retrieves the details of a movie using its Id.
     */
    fun getMovieDetails(movieId: Int) = resultLiveData {
        movieDetailsUseCase.getMovieDetails(movieId)
    }
}