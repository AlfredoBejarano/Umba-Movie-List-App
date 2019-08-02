package me.alfredobejarano.movieslist.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.alfredobejarano.movieslist.R
import me.alfredobejarano.movieslist.databinding.FragmentMovieListHubBinding
import me.alfredobejarano.movieslist.utils.replaceFragment

/**
 * Created by alfredo on 2019-08-02.
 * Copyright © 2019 GROW. All rights reserved.
 */
class MovieListHubFragment : Fragment() {
    private lateinit var dataBinding: FragmentMovieListHubBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMovieListHubBinding.inflate(inflater, container, false).also {
        dataBinding = it
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.hubBottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.popular ->
                    dataBinding.movieListFrameLayout.replaceFragment(
                        requireFragmentManager(),
                        PopularMovieListFragment()
                    )
                R.id.upcoming ->
                    dataBinding.movieListFrameLayout.replaceFragment(
                        requireFragmentManager(),
                        UpcomingMovieListFragment()
                    )
                R.id.top_rated ->
                    dataBinding.movieListFrameLayout.replaceFragment(
                        requireFragmentManager(),
                        TopRatedMovieListFragment()
                    )
                else -> false
            }
        }
    }
}