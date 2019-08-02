package me.alfredobejarano.movieslist.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import me.alfredobejarano.movieslist.core.Movie
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Model data class defining a Movie from TheMovieDB API.
 *
 * Values in this data class are defined as _nullable_ and
 * _optional_ because when the network data is not reliable and
 * the values are defined as non-null, they can be received as null
 * (because an incomplete JSON, for example), it can cause a
 * Kotlin.NullPointerException and we don't want that because we are writting
 * Kotlin.
 *
 * Created by alfredo on 2019-08-02.
 */
data class MovieResult(
    @Expose
    @SerializedName("id")
    val id: Int? = 0,
    @Expose
    @SerializedName("title")
    val title: String? = "",
    @Expose
    @SerializedName("poster_path")
    val posterPath: String? = "",
    @Expose
    @SerializedName("vote_average")
    val voteAverage: Double? = 0.0,
    @Expose
    @SerializedName("release_date")
    val releaseDate: String? = ""
) : TransformableModel<Movie> {
    companion object {
        private const val RELEASE_DATE_UI_FORMAT = "MMMM dd, yyyy"
        private const val RELEASE_DATE_REMOTE_FORMAT = "yyyy-MM-dd"
    }

    override fun transform() = Movie(
        id = id ?: 0,
        title = title ?: "",
        posterURL = posterPath ?: "",
        rating = getRatingPercentage(),
        releaseDate = getUIReleaseDate()
    )

    /**
     * The vote average value comes in a double that scale spans
     * from 0.0 to 10.0
     */
    private fun getRatingPercentage() = (voteAverage ?: 0.0 * 100).toInt()

    /**
     * Parses the remote date format into a human-readable value.
     */
    private fun getUIReleaseDate(): String = synchronized(this) {
        val uiFormatter = SimpleDateFormat(RELEASE_DATE_UI_FORMAT, Locale.getDefault())

        return try {
            uiFormatter.format(Date())
        } catch (e: Exception) {
            val date = SimpleDateFormat(RELEASE_DATE_REMOTE_FORMAT, Locale.getDefault())
                .parse(releaseDate ?: "2000-01-01") ?: Date()

            uiFormatter.format(date)
        }
    }
}