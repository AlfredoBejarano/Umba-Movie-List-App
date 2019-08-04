package me.alfredobejarano.movieslist.utils

import android.net.Uri
import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView

/**
 * Created by alfredo on 2019-08-02.
 */
abstract class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("posterURL")
        fun setPosterURL(simpleDraweeView: SimpleDraweeView, url: String) =
            simpleDraweeView.setImageURI(Uri.parse(url))
    }
}
