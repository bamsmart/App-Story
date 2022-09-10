package com.shinedev.digitalent.view.main

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.shinedev.digitalent.network.BaseResponse
import kotlinx.parcelize.Parcelize

data class ListStoryResponse(
    @field:SerializedName("listStory")
    val stories: List<StoryResponse> = listOf()
) : BaseResponse()

@Parcelize
data class StoryResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("lon")
    val lon: Double
) : Parcelable