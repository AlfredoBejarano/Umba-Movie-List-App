package me.alfredobejarano.movieslist.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.movieslist.R
import javax.inject.Inject

/**
 * Base class for [Fragment] that reduces boilerplate related to Presenter attachment and detachment
 * and UI error handling.
 */
open class BaseFragment<P : BasePresenter> : Fragment() {
    /**
     * Snackbar instance that shows any errors catch observing Presenter executions.
     */
    private var errorSnackBar: Snackbar? = null

    /**
     * Presenter attached to this class.
     */
    @Inject
    lateinit var presenter: P

    /**
     * Request injection of the presenter from the Dagger dependency graph and proceeds to
     * attach it to this class.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        presenter.onCreate(this)
    }

    /**
     * Creates the error Snackbar.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorSnackBar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)
    }

    /**
     * Shows an exception as an error using the [errorSnackBar].
     * @param e Exception that has been catch.
     * @param onRetry Function to execute when the retry button is pressed.
     */
    protected fun showError(e: Exception, onRetry: () -> Unit) =
        showError(String.format(getString(R.string.an_error_happened), e::class.java.name), onRetry)

    /**
     * Shows an error message using the [errorSnackBar].
     * @param message The error message to display
     * @param onRetry Function to execute when the retry button is pressed.
     */
    protected fun showError(message: String, onRetry: () -> Unit) {
        errorSnackBar?.setAction(R.string.retry) {
            onRetry()
        }?.setText(String.format(getString(R.string.an_error_happened), message))?.show()
    }

    /**
     * Destroys the presenter and the error snackbar in order to free resources.
     */
    override fun onDestroy() {
        super.onDestroy()
        errorSnackBar = null
        presenter.onDestroy()
    }
}