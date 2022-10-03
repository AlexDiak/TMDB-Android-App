package com.things.tothemovies.data.local.dao

import androidx.room.*
import com.things.tothemovies.data.local.model.Show
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM show WHERE title LIKE '%' || :query || '%'")
    suspend fun findByTitle(query: String): List<Show>

    @Query("SELECT * FROM show")
    suspend fun getAll(): List<Show>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(watchlistItem: Show)

    @Delete
    suspend fun delete(watchlistItem: Show)
}