package com.things.tothemovies.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.things.tothemovies.R

fun ImageView.setImage(url: String?) {
  Glide.with(this.context)
      .load("https://image.tmdb.org/t/p/w500/${url}".ifEmpty { null })
      .error(R.drawable.ic_launcher_background)
      .centerCrop()
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(this)
}