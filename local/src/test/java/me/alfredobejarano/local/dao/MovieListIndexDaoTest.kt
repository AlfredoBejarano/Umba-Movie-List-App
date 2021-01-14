package me.alfredobejarano.local.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import me.alfredobejarano.local.AppDatabase
import me.alfredobejarano.local.entity.MovieListIndex
import me.alfredobejarano.movieslist.core.MovieListType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class MovieListIndexDaoTest {
    private lateinit var testInMemoryDB: AppDatabase
    private lateinit var testCandidate: MovieListIndexDao

    @Before
    fun setup() {
        testInMemoryDB =
            Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, AppDatabase::class.java)
                .build()
        testCandidate = testInMemoryDB.provideMovieListIndexDao()
    }

    @Test
    fun createTest() = runBlocking {
        val id = MovieListType.MOVIE_LIST_POPULAR.ordinal
        val testSubject = MovieListIndex(id = id, movies = listOf(1))

        testCandidate.createOrUpdate(testSubject)

        val retrievedSubject = testCandidate.getListIndex(id).first()

        assert(retrievedSubject == testSubject)
    }

    @Test
    fun updateTest() = runBlocking {
        val testSubjectId = MovieListType.MOVIE_LIST_POPULAR.ordinal
        val testSubject = MovieListIndex(id = testSubjectId, movies = listOf(1))

        testCandidate.createOrUpdate(testSubject)
        val retrievedSubject = testCandidate.getListIndex(testSubjectId).first()

        assert(retrievedSubject.movies.size == 1)

        val updatedMovies = listOf(1, 2, 3)
        val updateTestSubject = MovieListIndex(id = testSubjectId, movies = updatedMovies)
        testCandidate.createOrUpdate(updateTestSubject)

        assert(retrievedSubject.movies.isNotEmpty())
    }

    @Test
    fun getListIndexTest() = runBlocking {
        val testSubjectId = Random.nextInt()
        val testSubject = MovieListIndex(id = testSubjectId, movies = listOf(1))

        testCandidate.createOrUpdate(testSubject)

        val retrievedSubject = testCandidate.getListIndex(testSubjectId).first()

        assert(retrievedSubject == testSubject)
    }

    @Test
    fun deleteTest() = runBlocking {
        val id = MovieListType.MOVIE_LIST_POPULAR.ordinal
        val testSubject = MovieListIndex(id = id, movies = listOf(1))

        testCandidate.createOrUpdate(testSubject)

        var index = testCandidate.getListIndex(id)
        assert(index.isNotEmpty())

        testCandidate.delete(testSubject)
        index = testCandidate.getListIndex(id)
        assert(index.isEmpty())
    }

    @After
    fun tearDown() = testInMemoryDB.close()
}