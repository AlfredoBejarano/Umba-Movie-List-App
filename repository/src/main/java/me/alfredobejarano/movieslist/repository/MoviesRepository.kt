package me.alfredobejarano.movieslist.repository

import me.alfredobejarano.local.dao.MovieDao
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiService

/**
 * Created by alfredo on 2019-08-02.
 */
class MoviesRepository(
    private val movieDaoDataSource: MovieDao,
    private val apiDataSource: TheMoviesDBApiService
) {
    suspend infix fun getMovieList(listType: MovieListType) {
        val movieList = when (listType) {
            MovieListType.MOVIE_LIST_POPULAR -> apiDataSource.getPopularMovies()
            MovieListType.MOVIE_LIST_UPCOMING -> apiDataSource.getUpcomingMovies()
            MovieListType.MOVIE_LIST_TOP_RATED -> apiDataSource.getTopRatedMovies()
        }
    }
}
