package me.alfredobejarano.movieslist.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import me.alfredobejarano.movieslist.core.Result

/**
 * Function that helps consolidating code about observing result LiveData classes.
 */
fun <T : Any> LiveData<Result<T>>.observeWith(
    owner: LifecycleOwner,
    isLoading: (Boolean) -> Unit = {},
    onSuccess: T.() -> Unit,
    onError: Exception.() -> Unit
) = observe(owner) { result ->
    when (result) {
        Result.Loading -> isLoading(true)
        is Result.Success -> {
            isLoading(false)
            result.data.onSuccess()
        }
        is Result.Error -> {
            isLoading(false)
            result.exception.onError()
        }
    }
}