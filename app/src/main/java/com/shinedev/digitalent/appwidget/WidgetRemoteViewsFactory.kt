package com.shinedev.digitalent.appwidget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.shinedev.digitalent.R
import com.shinedev.digitalent.data.database.entity.StoryEntity
import com.shinedev.digitalent.data.provider.DatabaseContract
import com.shinedev.digitalent.data.provider.DatabaseContract.getColumnString
import com.shinedev.digitalent.data.story.StoryModel
import com.shinedev.digitalent.common.withDateFormat


class WidgetRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<StoryModel>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val identityToken: Long = Binder.clearCallingIdentity()
        val mCursor: Cursor? = mContext.contentResolver.query(
            DatabaseContract.CONTENT_URI_STORY,
            null,
            null,
            null,
            null
        )
        mCursor?.count?.let { count ->
            for (i in 0 until count) {
                mCursor.moveToPosition(i)
                val id = getColumnString(mCursor, DatabaseContract.StoryColumns.ID)
                val name = getColumnString(mCursor, DatabaseContract.StoryColumns.NAME)
                val desc = getColumnString(mCursor, DatabaseContract.StoryColumns.DESCRIPTION)
                val image = getColumnString(mCursor, DatabaseContract.StoryColumns.IMAGE)
                val createdAt = getColumnString(mCursor, DatabaseContract.StoryColumns.CREATED_AT)
                val model = StoryEntity(id, name, desc, image, createdAt, 0.0, 0.0)
                try {
                    mWidgetItems.add(model)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        Binder.restoreCallingIdentity(identityToken)
    }


    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.collection_widget_list_item)
        val bitmap: Bitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].photoUrl)
            .circleCrop()
            .submit(48, 48)
            .get()
        rv.setImageViewBitmap(R.id.previewImageView, bitmap)
        rv.setTextViewText(R.id.widgetItemNameLabel, mWidgetItems[position].name)
        rv.setTextViewText(
            R.id.widgetItemCreatedTimeLabel,
            mWidgetItems[position].createdAt.withDateFormat()
        )

        val extras = bundleOf(
            CollectionWidgetProvider.UPDATE_WIDGET to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.previewImageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size
}