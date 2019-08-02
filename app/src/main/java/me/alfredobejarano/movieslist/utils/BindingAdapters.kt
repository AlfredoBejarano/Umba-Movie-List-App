package me.alfredobejarano.movieslist.utils

import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView

/**
 * Created by alfredo on 2019-08-02.
 * Copyright © 2019 GROW. All rights reserved.
 */
abstract class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("posterURL")
        fun setPosterURL(simpleDraweeView: SimpleDraweeView, url: String) =
            simpleDraweeView.setImageURI(url)
    }
}
