package com.shinedev.digitalent.view.main

import android.database.Cursor
import androidx.annotation.WorkerThread
import com.shinedev.digitalent.database.dao.StoryDao
import com.shinedev.digitalent.database.entity.StoryEntity

class StoryRepository(private val dao: StoryDao) {

    val listStory: Cursor = dao.getListStory()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun inserts(list: List<StoryEntity>) {
        dao.inserts(list)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}