package me.alfredobejarano.movieslist.remote

import okhttp3.Interceptor
import okhttp3.Request
import java.util.Locale

/**
 * Class that will add the common query params such as authorization token or language
 * to all the TheMoviesDB API endpoints requests.
 *
 * Created by alfredo on 2019-08-02.
 */
class TheMoviesDBApiAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = chain.run {
        proceed(request()).newBuilder().apply { buildRequest(request()) }.build()
    }

    private fun buildRequest(request: Request) =
        request.newBuilder().url(addQueryParamsToURL(request)).build()

    private fun addQueryParamsToURL(request: Request) = request.url.newBuilder().apply {
        val deviceRegion = getLanguageAndRegion()
        addQueryParameter("language", deviceRegion.first)
        addQueryParameter("region", deviceRegion.second)
        addQueryParameter("api_key", Properties.THE_MOVIE_DB_API_KEY)
    }.build()

    /**
     * Retrieves the language and country (region) from the current device
     * locale in ISO-639-1 format.
     *
     * @return [Pair] object containing the language in the first position
     * and the country in the second position. Ex: es, MX
     */
    private fun getLanguageAndRegion() = Locale.getDefault().run {
        Pair(language ?: "es", country.toUpperCase(this))
    }
}