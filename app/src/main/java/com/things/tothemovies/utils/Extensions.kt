package com.things.tothemovies.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.setImage(url: String?) {
    Glide.with(this.context)
        .load(url?.ifEmpty { null })
        .fitCenter()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}