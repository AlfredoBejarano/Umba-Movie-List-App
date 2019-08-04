package me.alfredobejarano.movieslist.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder
import me.alfredobejarano.movieslist.R

/**
 * Created by alfredo on 2019-08-02.
 */
abstract class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("posterURL")
        fun setPosterURL(simpleDraweeView: SimpleDraweeView, url: String) {
            simpleDraweeView.hierarchy.setPlaceholderImage(ColorDrawable(Color.parseColor("#2da8ff")))
            val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build()
            simpleDraweeView.setImageRequest(imageRequest)
        }
    }
}
