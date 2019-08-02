package me.alfredobejarano.movieslist.movielist.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.databinding.ItemMovieBinding
import me.alfredobejarano.movieslist.utils.layoutInflater

/**
 * [RecyclerView.Adapter] class that gives instructions to a [RecyclerView] of how
 * to render a list of [Movie] elements.
 *
 * It uses [DiffUtil] to maintain the code complexity as lower as possible.
 */
internal class MovieListAdapter(private var movies: List<Movie>) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieViewHolder(
            ItemMovieBinding.inflate(parent.layoutInflater, parent, false)
        )

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.binding.run {
        movie = movies[position]
        executePendingBindings()
    }

    fun updateList(newMovieList: List<Movie>) {
        val callback =
            MovieCallback(
                movies,
                newMovieList
            )
        val diff = DiffUtil.calculateDiff(callback)
        diff.dispatchUpdatesTo(this)
        movies = newMovieList
    }

    /**
     * Simple [RecyclerView.ViewHolder] class that represents a [Movie] in a [RecyclerView].
     */
    internal class MovieViewHolder(internal val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * [DiffUtil] callback that calculates the differences between two lists of
     * Movies and just updates the updated elements instead of the whole list.
     */
    internal class MovieCallback(
        private val oList: List<Movie>,
        private val nList: List<Movie>
    ) : DiffUtil.Callback() {
        override fun getNewListSize() = nList.size
        override fun getOldListSize() = oList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oList[oldItemPosition].id == nList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }
}