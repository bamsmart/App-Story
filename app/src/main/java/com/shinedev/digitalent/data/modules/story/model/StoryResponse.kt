package com.shinedev.digitalent.data.modules.story.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.shinedev.digitalent.data.remote.BaseResponse
import kotlinx.parcelize.Parcelize

data class ListStoryResponse(
    @field:SerializedName("listStory")
    val stories: List<StoryResponse> = listOf()
) : BaseResponse()

@Parcelize
data class StoryResponse(
    @field:SerializedName("id")
    override val id: String,

    @field:SerializedName("name")
    override val name: String,

    @field:SerializedName("description")
    override val description: String,

    @field:SerializedName("photoUrl")
    @ColumnInfo(name = "photo_url")
    override val photoUrl: String,

    @field:SerializedName("createdAt")
    @ColumnInfo(name = "created_at")
    override val createdAt: String,

    @field:SerializedName("lat")
    override val lat: Double,

    @field:SerializedName("lon")
    override val lon: Double
) : StoryModel, Parcelable