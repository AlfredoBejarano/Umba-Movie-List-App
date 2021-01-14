package me.alfredobejarano.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import me.alfredobejarano.local.AppDatabase
import me.alfredobejarano.movieslist.core.MovieDetails
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class MovieDetailsDaoTest {
    private lateinit var testInMemoryDB: AppDatabase
    private lateinit var testCandidate: MovieDetailsDao

    @Before
    fun setup() {
        testInMemoryDB =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            ).build()
        testCandidate = testInMemoryDB.provideMovieDetailsDao()
    }

    @Test
    fun createTest() = runBlocking {
        val testSubjectId = Random.nextInt()
        val testSubject = MovieDetails(id = testSubjectId)

        testCandidate.createOrUpdate(testSubject)

        val retrievedSubject = testCandidate.read(testSubjectId).first()

        assert(retrievedSubject.id == testSubject.id)
    }

    @Test
    fun updateTest() = runBlocking {
        val testSubjectId = Random.nextInt()
        val testSubject = MovieDetails(id = testSubjectId)

        testCandidate.createOrUpdate(testSubject)
        val retrievedSubject = testCandidate.read(testSubjectId).first()

        assert(retrievedSubject.title == "")

        val updatedTitle = "title"
        val updateTestSubject = MovieDetails(id = testSubjectId, title = updatedTitle)
        testCandidate.createOrUpdate(updateTestSubject)

        assert(testCandidate.read(testSubjectId).first().title == updatedTitle)
    }

    @Test
    fun readTest() = runBlocking {
        val testSubjectId = Random.nextInt()
        val testSubject = MovieDetails(id = testSubjectId)

        testCandidate.createOrUpdate(testSubject)

        val retrievedSubject = testCandidate.read(testSubjectId).first()

        assert(retrievedSubject.id == testSubject.id)
    }

    @After
    fun tearDown() = testInMemoryDB.close()
}