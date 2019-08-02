package me.alfredobejarano.movieslist.remote

/**
 * Created by alfredo on 2019-08-01.
 *
 */
object Properties {
    val THE_MOVIE_DB_API_KEY: String = System.getProperty("theMovieDBApiKey") ?: ""
    val THE_MOVIE_DB_API_BASE_URL: String = System.getProperty("theMovieDBBaseURL") ?: ""
}