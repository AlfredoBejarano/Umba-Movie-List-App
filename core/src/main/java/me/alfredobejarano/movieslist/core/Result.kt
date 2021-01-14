package me.alfredobejarano.movieslist.core

/**
 * Data class that transport a given payload from the Repository to the
 * rest of the layers. This class also helps to report the state of a
 * repository call.
 *
 * Created by alfredo on 2019-08-02.
 */
sealed class Result<out T : Any> {
    object Loading : Result<Nothing>()
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}