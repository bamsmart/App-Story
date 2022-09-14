package com.shinedev.digitalent.domain.story

import androidx.annotation.WorkerThread
import com.shinedev.digitalent.data.database.dao.StoryDao
import com.shinedev.digitalent.data.database.entity.StoryEntity

class StoryRepository(private val dao: StoryDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun inserts(list: List<StoryEntity>) {
        dao.inserts(list)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}