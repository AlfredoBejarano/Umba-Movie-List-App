package me.alfredobejarano.movieslist.utils

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.forEach
import androidx.databinding.ViewDataBinding

private const val CHILD_VIEW_FULL_ALPHA = 1f
private const val CHILD_VIEW_LOADING_ALPHA = 0.26f

/**
 * Shows or hides the needed views that notifies the user that the app is doing work.
 */
fun ViewDataBinding.isLoading(isLoading: Boolean) = (root as? ViewGroup)?.forEach { childView ->
    if (childView is ProgressBar) {
        childView.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    } else {
        childView.alpha = if (isLoading) CHILD_VIEW_LOADING_ALPHA else CHILD_VIEW_FULL_ALPHA
    }
} ?: Unit
