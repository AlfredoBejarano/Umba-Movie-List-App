package me.alfredobejarano.movieslist.movielist

import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.movielist.base.MovieListFragment

/**
 * Created by alfredo on 2019-08-02.
 */
class UpcomingMovieListFragment : MovieListFragment() {
    override val listType = MovieListType.MOVIE_LIST_UPCOMING
}