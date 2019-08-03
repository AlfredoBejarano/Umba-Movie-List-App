package me.alfredobejarano.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.alfredobejarano.local.dao.MovieDao
import me.alfredobejarano.local.dao.MovieListIndexDao
import me.alfredobejarano.local.entity.MovieListIndex
import me.alfredobejarano.local.typeconverters.IntListTypeConverter
import me.alfredobejarano.movieslist.core.Movie

/**
 * Created by alfredo on 2019-08-02.
 */
@Database(
    entities = [Movie::class, MovieListIndex::class],
    version = BuildConfig.VERSION_CODE,
    exportSchema = false
)
@TypeConverters(IntListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun provideMovieDao(): MovieDao
    abstract fun provideMovieListIndexDao(): MovieListIndexDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(ctx: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: createInstance(ctx).also { INSTANCE = it }
        }

        private fun createInstance(ctx: Context) =
            Room.databaseBuilder(
                ctx, AppDatabase::class.java,
                "${BuildConfig.APPLICATION_ID}.database"
            ).fallbackToDestructiveMigration().build()
    }
}