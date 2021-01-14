package me.alfredobejarano.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import me.alfredobejarano.local.AppDatabase
import me.alfredobejarano.movieslist.core.Movie
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {
    private lateinit var testCandidate: MovieDao
    private lateinit var testInMemoryDB: AppDatabase

    @Before
    fun setup() {
        testInMemoryDB =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            ).build()
        testCandidate = testInMemoryDB.provideMovieDao()
    }

    @Test
    fun createTest() = runBlocking {
        val testSubjectId = Random.nextInt()
        val testSubject = Movie(id = testSubjectId)

        testCandidate.createOrUpdate(testSubject)

        val retrievedSubject = testCandidate.read(testSubjectId).first()

        assert(retrievedSubject == testSubject)
    }

    @Test
    fun updateTest() = runBlocking {
        val testSubjectId = Random.nextInt()
        val testSubject = Movie(id = testSubjectId)

        testCandidate.createOrUpdate(testSubject)
        val retrievedSubject = testCandidate.read(testSubjectId).first()

        assert(retrievedSubject.title == "")

        val updatedTitle = "title"
        val updateTestSubject = Movie(id = testSubjectId, title = updatedTitle)
        testCandidate.createOrUpdate(updateTestSubject)

        assert(testCandidate.read(testSubjectId).first().title == updatedTitle)
    }

    @Test
    fun readTest() = runBlocking {
        val testSubjectId = Random.nextInt()
        val testSubject = Movie(id = testSubjectId)

        testCandidate.createOrUpdate(testSubject)

        val retrievedSubject = testCandidate.read(testSubjectId).first()

        assert(retrievedSubject == testSubject)
    }

    @Test
    fun findByTitleTest() = runBlocking {
        val testSubject = Movie(title = "My awesome movie")

        testCandidate.createOrUpdate(testSubject)

        var retrievedSubjects = testCandidate.findByTitle("My awesome movie")
        assert(retrievedSubjects.isNotEmpty())

        retrievedSubjects = testCandidate.findByTitle("ewe")
        assert(retrievedSubjects.isEmpty())
    }

    @Test
    fun deleteTest() = runBlocking {
        val testSubject = Movie()
        testCandidate.createOrUpdate(testSubject)

        var movies = testCandidate.read(0)
        assert(movies.isNotEmpty())

        testCandidate.delete(movies.first())
        movies = testCandidate.read(0)

        assert(movies.isEmpty())
    }

    @Test
    fun deleteAllTest() = runBlocking {
        val testSubject = Movie()
        testCandidate.createOrUpdate(testSubject)

        var movies = testCandidate.read(0)
        assert(movies.isNotEmpty())

        testCandidate.deleteAll()
        movies = testCandidate.read(0)

        assert(movies.isEmpty())
    }

    @After
    fun tearDown() = testInMemoryDB.close()
}