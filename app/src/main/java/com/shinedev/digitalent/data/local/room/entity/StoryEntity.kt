package com.shinedev.digitalent.data.local.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.shinedev.digitalent.data.modules.story.model.StoryModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoryEntity(
    @ColumnInfo(name = "id")
    @Expose
    @PrimaryKey
    override val id: String,

    @ColumnInfo(name = "name")
    @Expose
    override val name: String,

    @ColumnInfo(name = "description")
    @Expose
    override val description: String,

    @ColumnInfo(name = "photo_url")
    @Expose
    override val photoUrl: String,

    @ColumnInfo(name = "created_at")
    @Expose
    override val createdAt: String,

    @ColumnInfo(name = "lat")
    @Expose
    override val lat: Double,

    @ColumnInfo(name = "lon")
    @Expose
    override val lon: Double
) : StoryModel, Parcelable