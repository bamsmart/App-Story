package com.shinedev.digitalent.provider

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val TABLE_STORY = "story"
    const val CONTENT_AUTHORITY_STORY = "com.shinedev.digitalent.story"
    const val SCHEME = "content"

    val CONTENT_URI_STORY = Uri.Builder().scheme(SCHEME)
        .authority(CONTENT_AUTHORITY_STORY)
        .appendPath(TABLE_STORY)
        .build()

    @SuppressLint("Range")
    fun getColumnString(cursor: Cursor, columnName: String?): String {
        return cursor.getString(cursor.getColumnIndex(columnName))
    }

    @SuppressLint("Range")
    fun getColumnInt(cursor: Cursor, columnName: String?): Int {
        return cursor.getInt(cursor.getColumnIndex(columnName))
    }

    @SuppressLint("Range")
    fun getColumnLong(cursor: Cursor, columnName: String?): Long {
        return cursor.getLong(cursor.getColumnIndex(columnName))
    }

    @SuppressLint("Range")
    fun getColumnDouble(cursor: Cursor, columnName: String?): Double {
        return cursor.getDouble(cursor.getColumnIndex(columnName))
    }

    object StoryColumns : BaseColumns {
        const val ID = "id"
        const val TITLE = "title"
        const val IMAGE = "photo_url"
    }
}