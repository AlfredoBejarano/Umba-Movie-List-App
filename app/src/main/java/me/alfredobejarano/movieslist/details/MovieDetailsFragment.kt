package me.alfredobejarano.movieslist.details

import android.R.transition.explode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.movieslist.BuildConfig
import me.alfredobejarano.movieslist.databinding.FragmentMovieDetailsBinding
import me.alfredobejarano.movieslist.di.ViewModelFactory
import me.alfredobejarano.movieslist.utils.isLoading
import me.alfredobejarano.movieslist.utils.observeWith
import me.alfredobejarano.movieslist.utils.viewBinding
import javax.inject.Inject


class MovieDetailsFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory
    private var videoPlayer: YouTubePlayer? = null
    private lateinit var viewModel: MovieDetailsViewModel
    private val dataBinding by viewBinding(FragmentMovieDetailsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProvider(this, factory)[MovieDetailsViewModel::class.java]
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(explode)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        dataBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovieDetails(arguments?.getInt("movieId") ?: 0)
    }

    private fun getMovieDetails(movieId: Int) =
        viewModel.getMovieDetails(movieId).observeWith(viewLifecycleOwner, dataBinding::isLoading, {
            dataBinding.movie = this
            dataBinding.movieVideoFrameLayout.setOnClickListener { playYouTubeVideo(videoKey) }
        }, {})

    private fun playYouTubeVideo(videoKey: String) {
        dataBinding.movieVideoFrameLayout.removeAllViews()
        dataBinding.movieVideoFrameLayout.setOnClickListener(null)

        val fragment = YouTubePlayerSupportFragment.newInstance()
        fragment.initialize(BuildConfig.YOUTUBE_API_KEY, object : OnInitializedListener {
            override fun onInitializationSuccess(
                p: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                restored: Boolean
            ) {
                videoPlayer = player
                if (!restored) {
                    videoPlayer?.loadVideo(videoKey)
                }
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) = Unit
        })

        childFragmentManager.beginTransaction()
            .add(dataBinding.movieVideoFrameLayout.id, fragment, "VIDEO")
            .commitAllowingStateLoss()
    }

    override fun onPause() {
        super.onPause()
        if (videoPlayer?.isPlaying == true) {
            videoPlayer?.pause()
        }
    }
}