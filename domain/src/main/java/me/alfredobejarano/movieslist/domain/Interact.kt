package me.alfredobejarano.movieslist.domain

import me.alfredobejarano.movieslist.core.Result

/**
 * Runs a given block of code that should return any processing from (or using data from) one or
 * many repository classes.
 *
 * Any errors will be catch and returned via the [Result] class.
 */
internal suspend fun <T : Any> useCaseExecution(suspendWork: suspend () -> T): Result<T> = try {
    val payload = suspendWork()
    Result.Success(payload)
} catch (e: Exception) {
    Result.Error(e)
}