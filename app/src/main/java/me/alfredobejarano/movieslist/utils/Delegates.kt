package me.alfredobejarano.movieslist.utils

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Delegate function that handles a ViewBinding object lifecycle within a fragment to prevent
 * root view's leaks.
 */
fun <T> Fragment.viewBinding(initialize: (layoutInflater: LayoutInflater) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        init {
            this@viewBinding
                .viewLifecycleOwnerLiveData
                .observe(this@viewBinding, { owner: LifecycleOwner ->
                    owner.lifecycle.addObserver(this)
                })
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T = this.binding
            ?: if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                error("Called before onCreateView or after onDestroyView.")
            } else {
                initialize(layoutInflater).also {
                    this.binding = it
                }
            }

        override fun onCreate(owner: LifecycleOwner) = Unit

        override fun onStart(owner: LifecycleOwner) = Unit

        override fun onResume(owner: LifecycleOwner) = Unit

        override fun onPause(owner: LifecycleOwner) = Unit

        override fun onStop(owner: LifecycleOwner) = Unit
    }