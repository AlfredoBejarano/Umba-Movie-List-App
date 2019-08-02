package me.alfredobejarano.movieslist.remote

import retrofit2.http.GET

/**
 * Interface containing all the functions that allow
 * access to **TheMovieDB** API in Retrofit definitions.
 *
 * Created by alfredo on 2019-08-02.
 */
interface TheMoviesDBApiService {
    @GET("/movie/popular")
    suspend fun getPopularMovies()
}