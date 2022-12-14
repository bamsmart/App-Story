package com.shinedev.digitalent.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shinedev.digitalent.data.database.dao.StoryDao
import com.shinedev.digitalent.data.database.entity.StoryEntity

@Database(
    version = 1,
    entities = [
        StoryEntity::class],
    exportSchema = true
)
@TypeConverters(
    StoryConverter::class
)

abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

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