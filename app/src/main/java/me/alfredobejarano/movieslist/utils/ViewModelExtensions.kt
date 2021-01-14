package me.alfredobejarano.movieslist.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import me.alfredobejarano.movieslist.core.Result
import kotlin.coroutines.CoroutineContext

/**
 * Returns a [LiveData] that reports the execution of a ViewModel closable coroutine.
 * The given block of code is executed in [Dispatchers.IO]
 */
fun <T : Any> ViewModel.resultLiveData(
    context: CoroutineContext = Default,
    block: suspend () -> Result<T>
) = liveData(context) {
    emit(Result.Loading)
    val result = block()
    emit(result)
}