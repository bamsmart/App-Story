package com.shinedev.digitalent.appwidget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.shinedev.digitalent.R
import com.shinedev.digitalent.database.entity.StoryEntity
import com.shinedev.digitalent.provider.DatabaseContract
import com.shinedev.digitalent.provider.DatabaseContract.getColumnString
import com.shinedev.digitalent.view.main.StoryModel


class WidgetRemoteViewsFactory(private val mContext: Context, private val intent: Intent) :
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
                val title = getColumnString(mCursor, DatabaseContract.StoryColumns.TITLE)
                val image = getColumnString(mCursor, DatabaseContract.StoryColumns.IMAGE)
                val model = StoryEntity(0, title, image)

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
        //rv.setImageViewBitmap(R.id.previewImageView, mWidgetItems[position])
        rv.setTextViewText(R.id.widgetItemTaskNameLabel, mWidgetItems[position].title)

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