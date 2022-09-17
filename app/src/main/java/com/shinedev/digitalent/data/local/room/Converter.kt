package com.shinedev.digitalent.data.local.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shinedev.digitalent.data.local.room.entity.StoryEntity

class StoryConverter {
    @TypeConverter
    fun storedStringToListData(value: String): List<StoryEntity> {
        val type = object : TypeToken<List<StoryEntity>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun listDataToStoredString(list: List<StoryEntity>): String {
        return Gson().toJson(list)
    }
}