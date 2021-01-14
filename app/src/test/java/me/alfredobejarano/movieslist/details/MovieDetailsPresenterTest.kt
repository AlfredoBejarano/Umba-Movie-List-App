package me.alfredobejarano.movieslist.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import me.alfredobejarano.movieslist.core.MovieDetails
import me.alfredobejarano.movieslist.core.Result
import me.alfredobejarano.movieslist.domain.GetMovieDetailsUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieDetailsPresenterTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var mockUseCase: GetMovieDetailsUseCase

    @Mock
    private lateinit var mockObserver: Observer<in Result<MovieDetails>>

    private lateinit var testSubject: MovieDetailsPresenter

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        testSubject = MovieDetailsPresenter(mockUseCase)
    }

    @After
    @ExperimentalCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getMovieDetails(): Unit = runBlocking {
        val mockMovieId = 123
        val expected = Result.Success(MovieDetails(id = mockMovieId))

        Mockito.`when`(mockUseCase.getMovieDetails(mockMovieId))
            .thenReturn(expected)

        testSubject.getMovieDetails(mockMovieId).observeForever(mockObserver)

        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(Result.Loading)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(expected)
    }
}