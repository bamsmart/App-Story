package com.shinedev.digitalent

import com.shinedev.digitalent.data.local.room.entity.StoryEntity

object DataDummy {
    fun generateDummyStoryEntity(): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 0..10) {
            val story = StoryEntity(
                id = "$i",
                name = "Bams",
                description = "Cerita hari ini",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                createdAt = "2022-02-22T22:22:22Z",
                lat = 0.0,
                lon = 0.0
            )
            storyList.add(story)
        }
        return storyList
    }
}