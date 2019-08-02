package me.alfredobejarano.movieslist.movielist

import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.movielist.base.MovieListFragment

/**
 * Created by alfredo on 2019-08-02.
 * Copyright © 2019 GROW. All rights reserved.
 */
class PopularMovieListFragment : MovieListFragment() {
    override val listType = MovieListType.MOVIE_LIST_POPULAR
}