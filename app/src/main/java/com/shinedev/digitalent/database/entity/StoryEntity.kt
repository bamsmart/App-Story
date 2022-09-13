package com.shinedev.digitalent.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.shinedev.digitalent.view.main.StoryModel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "story")
data class StoryEntity(
    @ColumnInfo(name = "id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    @Expose
    override val title: String,

    @ColumnInfo(name = "photo_url")
    @Expose
    override val photoUrl: String
) : StoryModel, Parcelable