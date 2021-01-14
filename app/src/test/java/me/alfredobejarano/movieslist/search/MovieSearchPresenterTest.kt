package me.alfredobejarano.movieslist.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.Result
import me.alfredobejarano.movieslist.domain.SearchMovieByTitleUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MovieSearchPresenterTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var mockUseCase: SearchMovieByTitleUseCase

    @Mock
    private lateinit var mockObserver: Observer<in Result<List<Movie>>>

    private lateinit var testSubject: MovieSearchPresenter

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        testSubject = MovieSearchPresenter(mockUseCase)
    }

    @After
    @ExperimentalCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun searchMovieByTitle(): Unit = runBlocking {
        val mockTitle = "My awesome movie!"
        val expected = Result.Success(listOf(Movie(title = mockTitle)))

        Mockito.`when`(mockUseCase.searchMovieByTitle(mockTitle))
            .thenReturn(expected)

        testSubject.searchMovieByTitle(mockTitle).observeForever(mockObserver)

        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(Result.Loading)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(expected)
    }
}