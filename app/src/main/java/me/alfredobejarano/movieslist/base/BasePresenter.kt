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
interface BasePresenter : LifecycleObserver {
    /**
     * Attaches the give [LifecycleOwner] to this Presenter class.
     */
    fun onCreate(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    /**
     * Function called when the [LifecycleOwner] gets destroyed.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() = Unit
}