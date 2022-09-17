package com.shinedev.digitalent.data

import android.database.Cursor
import androidx.paging.PagingSource
import com.shinedev.digitalent.data.local.room.dao.StoryDao
import com.shinedev.digitalent.data.local.room.entity.StoryEntity
import com.shinedev.digitalent.data.modules.story.model.StoryResponse
import com.shinedev.digitalent.ui.story.StoryDataDummy
import com.shinedev.digitalent.utils.PagingSourceUtils

class FakeStoryDao : StoryDao {

    private var storyData = mutableListOf<StoryEntity>()

    override fun getTopStory(): Cursor {
        TODO()
    }

    override fun getPagedListStory(): PagingSource<Int, StoryResponse> {
        val response = StoryDataDummy.generateDummyStoryResponse()
        return PagingSourceUtils(response)
    }

    override fun getAllStory(): List<StoryResponse> {
        TODO("Not yet implemented")
    }

    override fun getStoryByID(id: String): List<StoryResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun inserts(data: List<StoryEntity?>) {
        data.forEach {
            it?.let { story ->
                storyData.add(story)
            }
        }
    }

    override suspend fun deleteAll() {
        storyData.clear()
    }

}