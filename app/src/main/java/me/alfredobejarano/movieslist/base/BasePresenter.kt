package me.alfredobejarano.movieslist.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * Base class that will represent a set of classes that will process data in a way that the UI
 * can present it.
 *
 * The view will be passed using Architecture component's [LifecycleOwner] interface.
 */
abstract class BasePresenter : LifecycleObserver {
    private var owner: LifecycleOwner? = null

    /**
     * Attaches the give [LifecycleOwner] to this Presenter class.
     */
    fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
        this.owner?.lifecycle?.addObserver(this)

    }

    /**
     * Function called when the [LifecycleOwner] gets destroyed.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        owner?.lifecycle?.removeObserver(this)
        owner = null
    }
}