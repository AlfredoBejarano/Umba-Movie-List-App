package me.alfredobejarano.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.alfredobejarano.movieslist.core.Movie

/**
 * Created by alfredo on 2019-08-02.
 */
@Database(entities = [Movie::class], version = BuildConfig.VERSION_CODE, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(ctx: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: createInstance(ctx).also { INSTANCE = it }
        }

        private fun createInstance(ctx: Context) =
            Room.databaseBuilder(
                ctx, AppDatabase::class.java,
                "${BuildConfig.LIBRARY_PACKAGE_NAME}.database"
            ).fallbackToDestructiveMigration().build()
    }
}