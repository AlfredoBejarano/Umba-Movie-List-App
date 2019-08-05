package me.alfredobejarano.movieslist.remote

import me.alfredobejarano.movieslist.remote.model.MovieResult
import me.alfredobejarano.movieslist.remote.model.MoviesListResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface containing all the functions that allow
 * access to **TheMovieDB** API in Retrofit definitions.
 *
 * Created by alfredo on 2019-08-02.
 */
interface TheMoviesDBApiService {

    /**
     * Retrieves a list of the most popular movies at the time.
     *
     * this list gets updated daily.
     */
    @GET("movie/popular")
    suspend fun getPopularMovies(): MoviesListResult

    /**
     * Retrieves a list of the top rated movies on TheMoviesDB.
     */
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): MoviesListResult

    /**
     * Get a list of upcoming movies in theatres.
     */
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): MoviesListResult

    /**
     * Retrieves the details of a given movie.
     * @param movieId Integer unique identifier for the movie.
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieResult

    /**
     * Searchs for movies with the title matching the given query.
     */
    @GET("search/movie")
    suspend fun searchMovie(@Query("query") query: String): MoviesListResult
}