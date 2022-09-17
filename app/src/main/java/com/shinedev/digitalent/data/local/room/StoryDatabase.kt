package com.shinedev.digitalent.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shinedev.digitalent.data.local.room.dao.RemoteKeysDao
import com.shinedev.digitalent.data.local.room.dao.StoryDao
import com.shinedev.digitalent.data.local.room.entity.RemoteKeys
import com.shinedev.digitalent.data.local.room.entity.StoryEntity

@Database(
    version = 1,
    entities = [
        StoryEntity::class,
        RemoteKeys::class],
    exportSchema = true
)
@TypeConverters(
    StoryConverter::class
)

abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        private const val DB_NAME = "DB_STORY_APP"

        @Volatile
        private var instance: StoryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): StoryDatabase {
            return Room.databaseBuilder(context, StoryDatabase::class.java, DB_NAME)
                .allowMainThreadQueries()
                .build()
        }
    }
}