package me.alfredobejarano.movieslist.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.core.Result
import me.alfredobejarano.movieslist.domain.GetMovieListUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieListPresenterTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var mockUseCase: GetMovieListUseCase

    @Mock
    private lateinit var mockObserver: Observer<in Result<List<Movie>>>

    private lateinit var testSubject: MovieListPresenter

    @Before
    @ExperimentalCoroutinesApi
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        testSubject = MovieListPresenter(mockUseCase)
    }

    @After
    @ExperimentalCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testGetMovieList(): Unit = runBlocking {
        val mockType = MovieListType.MOVIE_LIST_TOP_RATED
        val expected = Result.Success(emptyList<Movie>())

        Mockito.`when`(mockUseCase.getMovieList(mockType))
            .thenReturn(expected)

        testSubject.getMovieList(mockType)

        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(Result.Loading)
        Mockito.verify(mockObserver, Mockito.times(1)).onChanged(expected)
    }
}