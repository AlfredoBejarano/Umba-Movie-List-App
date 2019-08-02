package androidx.lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import me.alfredobejarano.movieslist.BuildConfig
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

private const val IO_JOB_KEY = "${BuildConfig.APPLICATION_ID}.IO_JOB_KEY"

/**
 *
 * [CoroutineScope] tied to this [ViewModel].
 * This scope will be canceled when ViewModel will be cleared, i.e [ViewModel.onCleared] is called
 *
 * This scope is bound to [Dispatchers.IO]
 *
 * Created by alfredo corona on 2019-08-02.
 */
val ViewModel.ioViewModelScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(IO_JOB_KEY)
        return scope?.let { safeScope ->
            safeScope
        } ?: run {
            setTagIfAbsent(IO_JOB_KEY, CloseableCoroutineScope(SupervisorJob() + Dispatchers.IO))
        }
    }

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override fun close() = coroutineContext.cancel()
    override val coroutineContext: CoroutineContext = context
}