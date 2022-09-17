package com.shinedev.digitalent.data

import com.shinedev.digitalent.data.modules.story.model.ListStoryResponse
import com.shinedev.digitalent.data.modules.story.StoryApiService
import com.shinedev.digitalent.data.remote.BaseResponse
import com.shinedev.digitalent.ui.story.StoryDataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody


class FakeStoryApiService : StoryApiService {
    private val dummyResponse = StoryDataDummy.getDummyListStoryResponse()

    override suspend fun addNewStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        long: Float
    ): Result<BaseResponse> {
        return Result.success(BaseResponse(error = false, message = "success"))
    }

    override suspend fun getListStories(
        token: String,
        page: Int,
        size: Int,
        location: Int
    ): Result<ListStoryResponse> {
        return Result.success(dummyResponse)
    }
}