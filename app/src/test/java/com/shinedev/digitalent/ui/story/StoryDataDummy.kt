package com.shinedev.digitalent.ui.story

import com.shinedev.digitalent.data.modules.authorization.model.LoginResponse
import com.shinedev.digitalent.data.modules.authorization.model.LoginResult
import com.shinedev.digitalent.data.modules.story.model.ListStoryResponse
import com.shinedev.digitalent.data.modules.story.model.StoryResponse
import com.shinedev.digitalent.data.remote.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object StoryDataDummy {
    fun generateDummyStoryResponse(): List<StoryResponse> {
        val storyList = ArrayList<StoryResponse>()
        for (i in 0..10) {
            val news = StoryResponse(
                id = "$i",
                name = "Bams",
                description = "Cerita hari ini",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                createdAt = "2022-02-22T22:22:22Z",
                lat = 0.0,
                lon = 0.0
            )
            storyList.add(news)
        }
        return storyList
    }

    fun getDummyListStoryResponse(): ListStoryResponse {
        val storyList = ArrayList<StoryResponse>()
        for (i in 0..10) {
            val story = StoryResponse(
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
        return ListStoryResponse(
            stories = storyList
        )
    }

    fun generateBaseResponse(): BaseResponse {
        return BaseResponse(
            error = false,
            message = "success"
        )
    }

    fun generateLoginResponse(): LoginResponse {
        return LoginResponse(
            result = LoginResult(
                userId = "user-W6AC8aGUsSMEKlXk",
                name = "bams",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVc2QUM4YUdVc1NNRUtsWGsiLCJpYXQiOjE2NjQzNzU4MTN9.aAgRYVV7uBwlU00dwmpP8nRDrAMn68DkaPisndfC0gc"
            )
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }
}