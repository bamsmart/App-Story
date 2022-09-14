package com.shinedev.digitalent.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
                Log.d("STORY","disiiix")
                storyDao.getListStory()
            }
            else -> null
        }
    }


    override fun update(uri: Uri, values: ContentValues?, extras: Bundle?): Int {
        val match = sUriMatcher.match(uri)
        val nrUpdated = 0
        try {
            if (match == STORY_ID) {
                val id: Int = uri.pathSegments[1].toInt()

            } else {
                throw UnsupportedOperationException("Unknown uri: $uri")
            }
        } finally {

        }
        if (nrUpdated != 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return nrUpdated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val count = 1
        return count
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        //
        return 0
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, contentValue: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        var returnUri: Uri

        return uri
    }
}