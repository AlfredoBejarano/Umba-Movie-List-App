package me.alfredobejarano.movieslist.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetails(
    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val runtime: Int,
    val votesCount: Int,
    val genres: List<String>,
    val posterURL: String,
    val overview: String,
    val videoKey: String
)