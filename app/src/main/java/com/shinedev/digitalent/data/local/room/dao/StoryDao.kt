package com.shinedev.digitalent.data.local.room.dao

import android.database.Cursor
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shinedev.digitalent.data.local.room.entity.StoryEntity
import com.shinedev.digitalent.data.modules.story.model.StoryResponse

@Dao
interface StoryDao {
    @Query("SELECT * FROM story LIMIT 5")
    fun getTopStory(): Cursor

    @Query("SELECT * FROM story")
    fun getPagedListStory(): PagingSource<Int, StoryResponse>

    @Query("SELECT * FROM story")
    fun getAllStory(): List<StoryResponse>

    @Query("SELECT * FROM story WHERE id = :id")
    fun getStoryByID(id: String): List<StoryResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserts(data: List<StoryEntity?>)

    @Query("DELETE FROM story")
    suspend fun deleteAll()

}