package com.things.tothemovies.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.things.tothemovies.data.local.model.Show

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM show WHERE title LIKE '%' || :query || '%'")
    fun findByTitle(query: String): PagingSource<Int, Show>

    @Query("SELECT EXISTS(SELECT * FROM show WHERE id = :id)")
    suspend fun showExists(id : Int) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(watchlistItem: Show)

    @Delete
    suspend fun delete(watchlistItem: Show)
}