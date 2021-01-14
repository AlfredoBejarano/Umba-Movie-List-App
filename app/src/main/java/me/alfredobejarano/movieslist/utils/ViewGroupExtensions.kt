package me.alfredobejarano.movieslist.utils

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Provides quick access to a [LayoutInflater] from a [ViewGroup] object.
 */
val ViewGroup.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)