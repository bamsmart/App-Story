package com.shinedev.digitalent.data.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shinedev.digitalent.data.database.entity.StoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM story LIMIT 5")
    fun getListStory(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserts(data: List<StoryEntity?>)

    @Query("DELETE FROM story")
    suspend fun deleteAll()

}