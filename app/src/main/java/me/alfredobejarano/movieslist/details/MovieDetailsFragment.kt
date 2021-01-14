package me.alfredobejarano.movieslist.details

import android.R.transition.explode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import me.alfredobejarano.movieslist.BuildConfig
import me.alfredobejarano.movieslist.base.BaseFragment
import me.alfredobejarano.movieslist.databinding.FragmentMovieDetailsBinding
import me.alfredobejarano.movieslist.utils.isLoading
import me.alfredobejarano.movieslist.utils.observeWith
import me.alfredobejarano.movieslist.utils.viewBinding

/**
 * Fragment that shows the details of a Movie
 */
class MovieDetailsFragment : BaseFragment<MovieDetailsPresenter>() {
    /**
     * YouTube video player that eill play the movie trailer (if available).
     */
    private var videoPlayer: YouTubePlayer? = null
    private val dataBinding by viewBinding(FragmentMovieDetailsBinding::inflate)

    /**
     * Shows a start-up transition.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(explode)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        dataBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovieDetails(arguments?.getInt("movieId") ?: 0)
    }

    /**
     * Uses a movie ID and retrieves it's metadata (such as rating, title, poster, trailer video)
     * using the [presenter] and displays it.
     *
     * @param movieId Id of the movie to look up for.
     */
    private fun getMovieDetails(movieId: Int) =
        presenter.getMovieDetails(movieId).observeWith(viewLifecycleOwner, dataBinding::isLoading, {
            dataBinding.movie = this
            dataBinding.movieVideoFrameLayout.setOnClickListener { playYouTubeVideo(videoKey) }
        }, {
            showError(this) { getMovieDetailsRetry(movieId) }
        })

    /**
     * Wrapper function that allows calling [getMovieDetails] if an error gets catch.
     */
    private fun getMovieDetailsRetry(movieId: Int) {
        getMovieDetails(movieId)
    }

    /**
     * Plays the movie trailer (if available).
     */
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

            /**
             * Shows the thrown error message and allows the user to try again to
             * play the video.
             */
            override fun onInitializationFailure(
                vieoProvider: YouTubePlayer.Provider?,
                initializationResult: YouTubeInitializationResult?
            ) = showError(initializationResult.toString()) {
                playYouTubeVideo(videoKey)
            }
        })

        childFragmentManager.beginTransaction()
            .add(dataBinding.movieVideoFrameLayout.id, fragment, "VIDEO")
            .commitAllowingStateLoss()
    }

    /**
     * Frees resources from the video player when this fragment goes to the background.
     */
    override fun onPause() {
        super.onPause()
        if (videoPlayer?.isPlaying == true) {
            videoPlayer?.pause()
        }
    }
}