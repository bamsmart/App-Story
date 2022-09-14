package com.shinedev.digitalent.appwidget

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.shinedev.digitalent.R
import com.shinedev.digitalent.view.detail.DetailStoryActivity
import com.shinedev.digitalent.view.main.MainActivity


class CollectionWidgetProvider : AppWidgetProvider() {

    companion object {
        const val UPDATE_WIDGET = "com.shinedev.digitalent.UPDATE_WIDGET"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(
                context.packageName,
                R.layout.stories_widget
            )

            // click event handler for the title, launches the app when the user clicks on title
            val titleIntent = Intent(context, MainActivity::class.java)
            val titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0)
            views.setOnClickPendingIntent(R.id.widgetTitleLabel, titlePendingIntent)


            val intent = Intent(context, WidgetRemoteViewsService::class.java)
            views.setRemoteAdapter(R.id.widgetListView, intent)

            val clickIntentTemplate = Intent(context, DetailStoryActivity::class.java)

            val clickPendingIntentTemplate: PendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.widgetListView, clickPendingIntentTemplate)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


    fun sendRefreshBroadcast(context: Context) {
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        intent.component = ComponentName(context, CollectionWidgetProvider::class.java)
        context.sendBroadcast(intent)
    }


    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            // refresh all your widgets
            val mgr = AppWidgetManager.getInstance(context)
            val cn = ComponentName(context, CollectionWidgetProvider::class.java)
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetListView)
        }
        super.onReceive(context, intent)
    }
}

