package com.shinedev.digitalent.data.local.provider

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val TABLE_STORY = "story"
    const val CONTENT_AUTHORITY_STORY = "com.shinedev.digitalent.story"
    private const val SCHEME = "content"

    val CONTENT_URI_STORY: Uri = Uri.Builder().scheme(SCHEME)
        .authority(CONTENT_AUTHORITY_STORY)
        .appendPath(TABLE_STORY)
        .build()

    @SuppressLint("Range")
    fun getColumnString(cursor: Cursor, columnName: String?): String {
        return cursor.getString(cursor.getColumnIndex(columnName))
    }

    object StoryColumns : BaseColumns {
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val IMAGE = "photo_url"
        const val CREATED_AT = "created_at"
    }
}