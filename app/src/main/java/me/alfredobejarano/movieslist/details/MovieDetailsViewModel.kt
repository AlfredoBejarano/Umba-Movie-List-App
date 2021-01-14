package me.alfredobejarano.movieslist.details

import androidx.lifecycle.ViewModel
import me.alfredobejarano.movieslist.utils.resultLiveData
import me.alfredobejarano.movieslist.domain.GetMovieDetailsUseCase
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val movieDetailsUseCase: GetMovieDetailsUseCase) :
    ViewModel() {
    /**
     * Retrieves the details of a movie using its Id.
     */
    fun getMovieDetails(movieId: Int) =
        resultLiveData { movieDetailsUseCase.getMovieDetails(movieId) }
}