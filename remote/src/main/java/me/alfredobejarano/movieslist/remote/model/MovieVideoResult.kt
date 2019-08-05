package me.alfredobejarano.movieslist.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieVideoResult(
    @Expose
    @SerializedName("")
    val key: String? = "key",
    @Expose
    @SerializedName("type")
    val type: String? = "",
    @Expose
    @SerializedName("site")
    val site: String? = ""
)