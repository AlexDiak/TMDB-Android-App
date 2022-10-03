package com.things.tothemovies.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Show(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "img_path") val posterPath: String?,
    @ColumnInfo(name = "media_type") val mediaType: String
)