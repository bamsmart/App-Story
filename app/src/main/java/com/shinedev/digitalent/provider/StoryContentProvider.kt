package com.shinedev.digitalent.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import com.shinedev.digitalent.database.StoryDatabase
import com.shinedev.digitalent.database.dao.StoryDao
import com.shinedev.digitalent.provider.DatabaseContract.CONTENT_AUTHORITY_STORY
import com.shinedev.digitalent.provider.DatabaseContract.TABLE_STORY

class StoryContentProvider : ContentProvider() {

    companion object {
        private val STORY = 1
        private val STORY_ID = 2
    }

    lateinit var database: StoryDatabase
    lateinit var storyDao: StoryDao

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(CONTENT_AUTHORITY_STORY, TABLE_STORY, STORY)
        addURI(
            CONTENT_AUTHORITY_STORY,
            "$TABLE_STORY/*",
            STORY_ID
        )
    }

    override fun onCreate(): Boolean {
        database = StoryDatabase.getInstance(context!!)
        storyDao = database.storyDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String?>?, selection: String?,
        selectionArgs: Array<String?>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            STORY -> {
                storyDao.getListStory()
            }
            else -> null
        }
    }


    override fun update(uri: Uri, values: ContentValues?, extras: Bundle?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, contentValue: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not yet implemented")
    }
}