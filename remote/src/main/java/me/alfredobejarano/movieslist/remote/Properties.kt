package me.alfredobejarano.movieslist.remote

/**
 * Created by alfredo on 2019-08-01.
 *
 */
object Properties {
    val DEBUG = getBuildConfigField<Boolean>("DEBUG") ?: true
    val THE_MOVIE_DB_API_KEY: String = System.getProperty("API_KEY") ?: ""
    val THE_MOVIE_DB_API_BASE_URL: String = System.getProperty("BASE_URL") ?: ""

    @Suppress("UNCHECKED_CAST")
    private fun <T> getBuildConfigField(fieldName: String): T? {
        val klass = Class.forName("me.alfredobejarano.movieslist")
        val field = klass.getDeclaredField(fieldName)
        return field.get(null) as? T
    }
}