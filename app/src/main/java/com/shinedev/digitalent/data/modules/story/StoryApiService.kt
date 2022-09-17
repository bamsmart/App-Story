package com.shinedev.digitalent.data.modules.story

import com.shinedev.digitalent.data.modules.story.model.ListStoryResponse
import com.shinedev.digitalent.data.remote.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryApiService {
    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") long: Float,
    ): Result<BaseResponse>

    @GET("stories")
    suspend fun getListStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 1
    ): Result<ListStoryResponse>
}