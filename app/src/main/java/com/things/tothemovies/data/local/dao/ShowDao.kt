package com.things.tothemovies.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.things.tothemovies.data.local.model.Show

@Dao
interface ShowDao {
    @Query("SELECT * FROM show")
    fun getAll(): List<Show>

    @Query("SELECT * FROM show WHERE uid IN (:showsIds)")
    fun loadAllByIds(showsIds: IntArray): List<Show>

    @Query("SELECT * FROM show WHERE title LIKE :first AND " +
            "title LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Show

    @Insert
    fun insertAll(vararg shows: Show)

    @Delete
    fun delete(show: Show)
}