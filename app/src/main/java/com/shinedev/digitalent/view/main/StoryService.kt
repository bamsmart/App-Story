package com.shinedev.digitalent.view.main

import com.shinedev.digitalent.network.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryService {
    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part photo:  MultipartBody.Part,
        @Part("description") description : RequestBody,
        @Part("lat") lat : Double,
        @Part("lon") long : Double,
    ): Call<BaseResponse>

    @GET("stories")
    fun getListStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 0
    ): Call<ListStoryResponse>
}