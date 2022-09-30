package com.things.tothemovies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.things.tothemovies.data.local.dao.ShowDao
import com.things.tothemovies.data.local.model.Show

@Database(entities = [Show::class], version = 1)
abstract class ShowDatabase : RoomDatabase() {
    abstract fun showDao(): ShowDao
}