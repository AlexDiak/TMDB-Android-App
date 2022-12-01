package com.things.tothemovies.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.things.tothemovies.data.local.dao.WatchlistDao
import com.things.tothemovies.data.local.model.Show

@Database(entities = [Show::class], version = 2, exportSchema = false)
abstract class WatchlistDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao

    companion object {

        @Volatile private var instance: WatchlistDatabase? = null

        fun getInstance(context: Context): WatchlistDatabase {
            return instance ?: synchronized(this) {
                return Room.databaseBuilder(context, WatchlistDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}