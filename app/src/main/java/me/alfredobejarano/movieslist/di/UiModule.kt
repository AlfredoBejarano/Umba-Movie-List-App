package me.alfredobejarano.movieslist.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.alfredobejarano.movieslist.NavHostActivity
import me.alfredobejarano.movieslist.movielist.PopularMovieListFragment
import me.alfredobejarano.movieslist.movielist.TopRatedMovieListFragment
import me.alfredobejarano.movieslist.movielist.UpcomingMovieListFragment
import me.alfredobejarano.movieslist.movielist.base.MovieListFragment

@Module
abstract class UiModule {
    @ContributesAndroidInjector
    abstract fun contributeNavHostActivity(): NavHostActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment

    @ContributesAndroidInjector
    abstract fun contributeUpcomingMovieListFragment(): UpcomingMovieListFragment

    @ContributesAndroidInjector
    abstract fun contributeTopRatedMovieListFragment(): TopRatedMovieListFragment

    @ContributesAndroidInjector
    abstract fun contributePopularMovieListFragment(): PopularMovieListFragment
}