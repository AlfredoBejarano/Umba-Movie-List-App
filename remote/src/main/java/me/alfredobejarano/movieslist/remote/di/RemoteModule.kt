package me.alfredobejarano.movieslist.remote.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import me.alfredobejarano.movieslist.remote.Properties
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiAuthInterceptor
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by alfredo on 2019-08-02.
 */
@Module
class RemoteModule {
    private val authInterceptor by lazy {
        TheMoviesDBApiAuthInterceptor()
    }

    private val gson: Gson by lazy { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }

    private val gsonConverter: GsonConverterFactory by lazy { GsonConverterFactory.create(gson) }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(gsonConverter)
            .baseUrl(Properties.THE_MOVIE_DB_API_BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideTheMoviesDBApiService(): TheMoviesDBApiService =
        retrofit.create(TheMoviesDBApiService::class.java)
}