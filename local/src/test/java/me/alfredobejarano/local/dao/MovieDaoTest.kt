package me.alfredobejarano.local.dao

import android.os.Build
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.local.AppDatabase
import me.alfredobejarano.movieslist.core.Movie
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.random.Random

@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.LOLLIPOP])
@RunWith(RobolectricTestRunner::class)
class MovieDaoTest {
    private val scope = GlobalScope
    private lateinit var testCandidate: MovieDao
    private lateinit var testInMemoryDB: AppDatabase

    @Before
    fun setup() {
        testInMemoryDB = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, AppDatabase::class.java).build()
        testCandidate = testInMemoryDB.provideMovieDao()
    }

    @Test
    fun createTest() {
        scope.launch {
            val testSubjectId = Random.nextInt()
            val testSubject = Movie(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)

            val retrievedSubject = testCandidate.read(testSubjectId)

            assert(retrievedSubject == testSubject)
        }
    }

    @After
    fun tearDown() = testInMemoryDB.close()
}