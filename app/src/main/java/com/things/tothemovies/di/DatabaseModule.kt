package com.things.tothemovies.di

import android.content.Context
import com.things.tothemovies.data.local.WatchlistDatabase
import com.things.tothemovies.data.local.dao.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideWatchlistDatabase(@ApplicationContext context: Context): WatchlistDatabase {
        return WatchlistDatabase.getInstance(context)
    }

    @Provides
    fun provideWatchlistDao(watchlistDatabase: WatchlistDatabase): WatchlistDao {
        return watchlistDatabase.watchlistDao()
    }
}