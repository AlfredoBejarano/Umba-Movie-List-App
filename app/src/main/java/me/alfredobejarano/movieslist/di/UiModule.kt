package me.alfredobejarano.movieslist.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.alfredobejarano.movieslist.NavHostActivity
import me.alfredobejarano.movieslist.movielist.MovieListFragment

@Module
abstract class UiModule {
    @ContributesAndroidInjector
    abstract fun contributeNavHostActivity(): NavHostActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment
}