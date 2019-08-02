package me.alfredobejarano.movieslist.repository

import me.alfredobejarano.local.CachesLifeManager
import me.alfredobejarano.local.dao.MovieDao
import me.alfredobejarano.local.dao.MovieListIndexDao
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiService
import me.alfredobejarano.movieslist.remote.map.Mapper
import me.alfredobejarano.movieslist.remote.model.MovieResult

/**
 * Created by alfredo on 2019-08-02.
 */
class MoviesListRepository(
    private val movieDaoDataSource: MovieDao,
    private val mapper: Mapper<MovieResult, Movie>,
    private val cachesLifeManager: CachesLifeManager,
    private val apiDataSource: TheMoviesDBApiService,
    private val movieListIndexMovieDao: MovieListIndexDao
) {
    /**
     * Retrieves the list of movies by a given type.
     *
     * If the cache for the given list is still valid, it retrieves the
     * cached values from the local database, if the cache has expired, it
     * proceeds to retrieve it from the remote data source.
     *
     * @see MovieListType
     */
    suspend infix fun getMoviesListBy(type: MovieListType) =
        if (cachesLifeManager.listCacheIsValid(type)) {
            getMoviesListFromLocal(type)
        } else {
            getMovieListFromRemote(type).also {
                cachesLifeManager.generateListCache(type)
            }
        }

    /**
     * Retrieves the cached Movies.
     *
     * First, it proceeds to retrieve the index for the given move list and then
     * proceeds to read the cached movie data from the index movies Ids.
     */
    private suspend infix fun getMoviesListFromLocal(listType: MovieListType): List<Movie> {
        val movieListIndex = movieListIndexMovieDao.getListIndex(listType.ordinal)
        return movieListIndex.movies.map { movieId ->
            movieDaoDataSource.read(movieId)
        }
    }

    /**
     * Calls the appropiate endpoint for retrieving a list of movies.
     */
    private suspend infix fun getMovieListFromRemote(listType: MovieListType) = when (listType) {
        MovieListType.MOVIE_LIST_POPULAR -> apiDataSource.getPopularMovies().results
        MovieListType.MOVIE_LIST_UPCOMING -> apiDataSource.getUpcomingMovies().results
        MovieListType.MOVIE_LIST_TOP_RATED -> apiDataSource.getTopRatedMovies().results
    }?.map(mapper::map) ?: emptyList()
}
